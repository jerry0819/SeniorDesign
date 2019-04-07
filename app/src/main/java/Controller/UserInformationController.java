package Controller;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.stitch.core.auth.providers.google.GoogleCredential;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.local.LocalMongoDbService;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;
import com.mongodb.stitch.core.services.mongodb.remote.sync.DefaultSyncConflictResolvers;


import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import Models.User;
import Util.SharedPreferenceHelper;

public class UserInformationController {
  private Context mContext;
  private User u;
  private SharedPreferenceHelper mSharedPreferenceHelper;
  private GoogleSignInAccount account;
  private final StitchAppClient client;
  private final RemoteMongoClient mongoClient;
  private final RemoteMongoCollection<Document> coll;

  public UserInformationController(Context context) {
    mContext = context;

    client = Stitch.getDefaultAppClient();
    mSharedPreferenceHelper = new SharedPreferenceHelper(context);
    mongoClient = client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

    coll = mongoClient.getDatabase("photoFoodDB").getCollection("photoFoodColl");

  }

  public void createUser(String email, String fname, String lname, int weight, int heightFeet, int heightInches, int age, boolean smoker, char sex){
    u = new User(email, fname, lname, weight, heightFeet, heightInches, age, smoker, sex);
    Document filterDoc = new Document().append("owner_id", client.getAuth().getUser().getId());
    Document updateDoc = new Document().append("$set",
            new Document()
                    .append("fname", fname)
                    .append("lname", lname)
                    .append("email", email)
                    .append("weight", weight)
                    .append("height", heightInches + (heightFeet*12))
                    .append("age", age)
                    .append("smoker", smoker)
                    .append("sex", sex)
    );


    final Task <RemoteUpdateResult> insertTask = coll.updateOne(filterDoc, updateDoc);
    insertTask.addOnCompleteListener(new OnCompleteListener <RemoteUpdateResult> () {
      @Override
      public void onComplete(@NonNull Task <RemoteUpdateResult> task) {
        if (task.isSuccessful()) {
          Log.d("app", String.format("successfully inserted item with id %s",
                  task.getResult().getUpsertedId()));
        } else {
          Log.e("app", "failed to insert document with: ", task.getException());
        }
      }
    });
  }
}