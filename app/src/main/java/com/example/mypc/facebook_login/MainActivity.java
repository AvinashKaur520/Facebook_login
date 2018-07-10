package com.example.mypc.facebook_login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity
{

    private CallbackManager callbackManager;

    private TextView textView;

    private AccessTokenTracker accessTokenTracker;

    private ProfileTracker profileTracker;

    LoginButton loginButton;

    ImageView imageView;

    FacebookCallback<LoginResult> callback;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();


        accessTokenTracker = new AccessTokenTracker()
        {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken1)
            {
            }

        };


        profileTracker = new ProfileTracker()
        {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile profile1)
            {

                displayMessage(profile1);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();


        callback = new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {

                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                displayMessage(profile);
            }

            @Override
            public void onCancel()
            {

            }

            @Override
            public void onError(FacebookException e)
            {

            }
        };


        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, callback);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onStop()
    {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }


    @Override
    protected void onPostResume()
    {
        super.onPostResume();

        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }

    private void displayMessage(Profile profile)
    {

        if (profile != null)
        {
            textView.setText(profile.getName());
            String url = profile.getProfilePictureUri(150, 150).toString();

            Glide.with(MainActivity.this).load(url).asBitmap()
                    .into(new BitmapImageViewTarget(imageView)
                    {
                        @Override
                        protected void setResource(Bitmap resource)
                        {
                            RoundedBitmapDrawable cd = RoundedBitmapDrawableFactory.
                                    create(getApplicationContext().getResources(), resource);
                            cd.setCircular(true);
                            imageView.setImageDrawable(cd);
                        }
                    });
        }
    }
}
