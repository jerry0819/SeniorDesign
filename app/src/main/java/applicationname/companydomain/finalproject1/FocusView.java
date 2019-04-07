package applicationname.companydomain.finalproject1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

class FocusView extends View {
    private int mSize;
    private int mCenterX;
    private int mCenterY;
    private int mLength;
    private Paint mPaint;

    public FocusView(Context context, int size) {
        this(context);
        init(size);
    }

    public FocusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSize = context.getResources().getDimensionPixelSize(R.dimen.focus_view_size);
        init(mSize);
    }

    private FocusView(Context context) {
        super(context);
    }

    private void init(int size) {
        mSize = size;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCenterX = (int) (mSize / 2.0);
        mCenterY = (int) (mSize / 2.0);
        mLength = (int) (mSize / 2.0) - 2;
        setMeasuredDimension(mSize, mSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
