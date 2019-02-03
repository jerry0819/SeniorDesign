package applicationname.companydomain.finalproject1;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.core.auth.providers.google.GoogleCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.auth.StitchUser;

import Util.SharedPreferenceHelper;

public class LoginActivity extends AppCompatActivity{
    private GoogleSignInClient mGoogleSignInClient;
    private  TextView tv;
    private int RC_SIGN_IN = 100;
    private SharedPreferenceHelper mSharedPreferenceHelper;
    private  StitchAppClient client;
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode("617401619813-vkou7fta5rirtfol1uul4gg1v8ri6kmc.apps.googleusercontent.com")
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);
        mSharedPreferenceHelper = new SharedPreferenceHelper(this);
        Log.e("CODE: ", "onCreate: " + mSharedPreferenceHelper.getCode());
        if(mSharedPreferenceHelper.getCode()!=""){
            gotoCameraScreen();
        }
        SignInButton signInButton = findViewById(R.id.sign_in_button);

        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick1(v);
            }
        });
       tv = findViewById(R.id.sign_in_text);

    }

    private void gotoCameraScreen() {
        Intent loadCamera = new Intent(this, PractiveCamera2Activity.class);
        startActivity(loadCamera);
    }

    private void updateUI(GoogleSignInAccount account) {
        tv.setText("Welcome " + account.getDisplayName() + "/" + account.getEmail());
        mSharedPreferenceHelper.saveCode(client.getAuth().getUser().getId());
        Intent loadUserName = new Intent(this, UserNameActivity.class);
        loadUserName.putExtra("email", account.getEmail());
        startActivity(loadUserName);
    }

    // Login Button onClick Handler
    public void onClick1(final View ignored) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
            return;
        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
             client = Stitch.initializeDefaultAppClient("clustertest-sfown");
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            final GoogleCredential googleCredential =
                    new GoogleCredential(account.getServerAuthCode());
            client.getAuth().loginWithCredential(googleCredential).addOnCompleteListener(
                    new OnCompleteListener<StitchUser>() {
                        @Override
                        public void onComplete(@NonNull final Task<StitchUser> task) {
                            if (task.isSuccessful()) {
                                updateUI(account);
                                // Do something here if the user logged in succesfully.
                            } else {
                                Log.e("ERROR", "Error logging in with Google", task.getException());
                            }
                        }
                    });
        } catch (ApiException e) {
            Log.w("ERROR", "signInResult:failed code=" + e.getStatusCode());
        }
    }


}
