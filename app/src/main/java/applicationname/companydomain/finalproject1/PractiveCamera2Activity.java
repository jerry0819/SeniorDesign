package applicationname.companydomain.finalproject1;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.MeteringRectangle;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

public class PractiveCamera2Activity extends AppCompatActivity implements View.OnClickListener {
    public static Bitmap bitmap;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout mDrawer;
    private Camera2BasicFragment mCamera2Fragment;
    private Toolbar toolbar;

    private Button takePicBtn;
    private static final int PICK_IMAGE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION |
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        takePicBtn = (Button) findViewById(R.id.picture);
        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nvView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawer.closeDrawers();
                int itemId = menuItem.getItemId();
                //Toast.makeText(getApplicationContext(), menuItem.getTitle().toString(),
                //       Toast.LENGTH_LONG).show();
                int id = menuItem.getItemId();
                if (id == R.id.nav_upload) {
                    Intent pictureScreen = new Intent(PractiveCamera2Activity.this, PictureActivity.class);
                    pictureScreen.putExtra("upload", true);
                    startActivity(pictureScreen);
                } else if (id == R.id.nav_search) {
                    Intent searchScreen = new Intent(PractiveCamera2Activity.this, SearchFoodActivity.class);
                    startActivity(searchScreen);
                } else if (id == R.id.nav_settings) {
                    Intent userInfoScreen = new Intent(PractiveCamera2Activity.this, UserFoodInfoActivity.class);
                    startActivity(userInfoScreen);
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        if (null == savedInstanceState) {
            mCamera2Fragment = Camera2BasicFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, mCamera2Fragment)
                    .commit();
        } else {
            mCamera2Fragment = (Camera2BasicFragment) getFragmentManager().findFragmentById(R.id.container);
        }

        takePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "onClick: AM I PRESSING SOMETHOING?" );
                mCamera2Fragment.takePicture();
            }
        });
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }
    public void load(){

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public void onClick(View view) {

        Log.e("TAKE PIC", "onClick: " );
        switch (view.getId()) {
            case R.id.picture: {
                mCamera2Fragment.takePicture();
                break;
            }
        }
    }


    public static class AutoFocusHelper{
            /**
             * camera2 API metering region weight.
             */
            private static final int CAMERA2_REGION_WEIGHT = (int)
                    (CameraUtil.lerp(MeteringRectangle.METERING_WEIGHT_MIN,
                            MeteringRectangle.METERING_WEIGHT_MAX,
                            CameraConstants.METERING_REGION_FRACTION));

            /**
             * Zero weight 3A region, to reset regions per API.
             */
            private static final MeteringRectangle[] ZERO_WEIGHT_3A_REGION = new MeteringRectangle[]{
                    new MeteringRectangle(0, 0, 0, 0, 0)
            };

            public static MeteringRectangle[] getZeroWeightRegion() {
                return ZERO_WEIGHT_3A_REGION;
            }

            /**
             * Compute 3A regions for a sensor-referenced touch coordinate.
             * Returns a MeteringRectangle[] with length 1.
             *
             * @param nx                x coordinate of the touch point, in normalized portrait
             *                          coordinates.
             * @param ny                y coordinate of the touch point, in normalized portrait
             *                          coordinates.
             * @param fraction          Fraction in [0,1]. Multiplied by min(cropRegion.width(),
             *                          cropRegion.height())
             *                          to determine the side length of the square MeteringRectangle.
             * @param cropRegion        Crop region of the image.
             * @param sensorOrientation sensor orientation as defined by
             *                          CameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION).
             */
            private static MeteringRectangle[] regionsForNormalizedCoord(
                    float nx, float ny, float fraction, final Rect cropRegion, int sensorOrientation) {
                // Compute half side length in pixels.
                int minCropEdge = Math.min(cropRegion.width(), cropRegion.height());
                int halfSideLength = (int) (0.5f * fraction * minCropEdge);
                // Compute the output MeteringRectangle in sensor space.
                // nx, ny is normalized to the screen.
                // Crop region itself is specified in sensor coordinates.
                // Normalized coordinates, now rotated into sensor space.
                PointF nsc = CameraUtil.normalizedSensorCoordsForNormalizedDisplayCoords(
                        nx, ny, sensorOrientation);
                int xCenterSensor = (int) (cropRegion.left + nsc.x * cropRegion.width());
                int yCenterSensor = (int) (cropRegion.top + nsc.y * cropRegion.height());
                Rect meteringRegion = new Rect(xCenterSensor - halfSideLength,
                        yCenterSensor - halfSideLength,
                        xCenterSensor + halfSideLength,
                        yCenterSensor + halfSideLength);
                // Clamp meteringRegion to cropRegion.
                meteringRegion.left = CameraUtil.clamp(meteringRegion.left, cropRegion.left,
                        cropRegion.right);
                meteringRegion.top = CameraUtil.clamp(meteringRegion.top, cropRegion.top,
                        cropRegion.bottom);
                meteringRegion.right = CameraUtil.clamp(meteringRegion.right, cropRegion.left,
                        cropRegion.right);
                meteringRegion.bottom = CameraUtil.clamp(meteringRegion.bottom, cropRegion.top,
                        cropRegion.bottom);
                return new MeteringRectangle[]{new MeteringRectangle(meteringRegion,
                        CAMERA2_REGION_WEIGHT)};
            }

            /**
             * Return AF region(s) for a sensor-referenced touch coordinate.
             * <p>
             * <p>
             * Normalized coordinates are referenced to portrait preview window with
             * (0, 0) top left and (1, 1) bottom right. Rotation has no effect.
             * </p>
             *
             * @return AF region(s).
             */
            public static MeteringRectangle[] afRegionsForNormalizedCoord(
                    float nx, float ny, final Rect cropRegion, int sensorOrientation) {
                return regionsForNormalizedCoord(nx, ny, CameraConstants.METERING_REGION_FRACTION,
                        cropRegion, sensorOrientation);
            }

            /**
             * Return AE region(s) for a sensor-referenced touch coordinate.
             * <p>
             * <p>
             * Normalized coordinates are referenced to portrait preview window with
             * (0, 0) top left and (1, 1) bottom right. Rotation has no effect.
             * </p>
             *
             * @return AE region(s).
             */
            public static MeteringRectangle[] aeRegionsForNormalizedCoord(
                    float nx, float ny, final Rect cropRegion, int sensorOrientation) {
                return regionsForNormalizedCoord(nx, ny, CameraConstants.METERING_REGION_FRACTION,
                        cropRegion, sensorOrientation);
            }


            /**
             * Calculates sensor crop region for a zoom level (zoom >= 1.0).
             *
             * @return Crop region.
             */
            public static Rect cropRegionForZoom(CameraCharacteristics characteristics, float zoom) {
                Rect sensor = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
                int xCenter = sensor.width() / 2;
                int yCenter = sensor.height() / 2;
                int xDelta = (int) (0.5f * sensor.width() / zoom);
                int yDelta = (int) (0.5f * sensor.height() / zoom);
                return new Rect(xCenter - xDelta, yCenter - yDelta, xCenter + xDelta, yCenter + yDelta);
            }
    }

    public static class CameraConstants {

        public static final long AUTO_FOCUS_TIMEOUT_MS = 800;  //800ms timeout, Under normal circumstances need to a few hundred milliseconds

        public static final long OPEN_CAMERA_TIMEOUT_MS = 2500;  //2.5s

        public static final int FOCUS_HOLD_MILLIS = 3000;

        public static final float METERING_REGION_FRACTION = 0.1225f;

        public static final int ZOOM_REGION_DEFAULT = 1;

        public static final int FLASH_OFF = 0;
        public static final int FLASH_ON = 1;
        public static final int FLASH_TORCH = 2;
        public static final int FLASH_AUTO = 3;
        public static final int FLASH_RED_EYE = 4;

        public static final int FACING_BACK = 0;
        public static final int FACING_FRONT = 1;
    }

    public static class CameraUtil {

        /**
         * Clamps x to between min and max (inclusive on both ends, x = min --> min,
         * x = max --> max).
         */
        public static int clamp(int x, int min, int max) {
            if (x > max) {
                return max;
            }
            if (x < min) {
                return min;
            }
            return x;
        }

        /**
         * Clamps x to between min and max (inclusive on both ends, x = min --> min,
         * x = max --> max).
         */
        public static float clamp(float x, float min, float max) {
            if (x > max) {
                return max;
            }
            if (x < min) {
                return min;
            }
            return x;
        }

        public static void inlineRectToRectF(RectF rectF, Rect rect) {
            rect.left = Math.round(rectF.left);
            rect.top = Math.round(rectF.top);
            rect.right = Math.round(rectF.right);
            rect.bottom = Math.round(rectF.bottom);
        }

        public static Rect rectFToRect(RectF rectF) {
            Rect rect = new Rect();
            inlineRectToRectF(rectF, rect);
            return rect;
        }

        public static RectF rectToRectF(Rect r) {
            return new RectF(r.left, r.top, r.right, r.bottom);
        }

        /**
         * Linear interpolation between a and b by the fraction t. t = 0 --> a, t =
         * 1 --> b.
         */
        public static float lerp(float a, float b, float t) {
            return a + t * (b - a);
        }

        /**
         * Given (nx, ny) \in [0, 1]^2, in the display's portrait coordinate system,
         * returns normalized sensor coordinates \in [0, 1]^2 depending on how the
         * sensor's orientation \in {0, 90, 180, 270}.
         * <p>
         * Returns null if sensorOrientation is not one of the above.
         * </p>
         */
        public static PointF normalizedSensorCoordsForNormalizedDisplayCoords(
                float nx, float ny, int sensorOrientation) {
            switch (sensorOrientation) {
                case 0:
                    return new PointF(nx, ny);
                case 90:
                    return new PointF(ny, 1.0f - nx);
                case 180:
                    return new PointF(1.0f - nx, 1.0f - ny);
                case 270:
                    return new PointF(1.0f - ny, nx);
                default:
                    return null;
            }
        }
    }
}
