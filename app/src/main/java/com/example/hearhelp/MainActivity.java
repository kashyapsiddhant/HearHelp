package com.example.hearhelp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private Button buttonOpenWebsite;
    private Button buttonSendEmail;
    private Button buttonOpenGoogleMaps;
    private Button buttonOpenGoogleContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TextToSpeech engine
        tts = new TextToSpeech(this, this);

        // Find the buttons in the layout
        buttonOpenWebsite = findViewById(R.id.buttonOpenWebsite);
        buttonSendEmail = findViewById(R.id.buttonSendEmail);
        buttonOpenGoogleMaps = findViewById(R.id.buttonOpenGoogleMaps);
        buttonOpenGoogleContacts = findViewById(R.id.buttonOpenGoogleContacts);

        // Set click event listeners for the buttons
        buttonOpenWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebsite();
            }
        });

        buttonSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });

        buttonOpenGoogleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGoogleMaps();
            }
        });

        buttonOpenGoogleContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGoogleContacts();
            }
        });
    }

    private void openWebsite() {
        // Perform the action to open a website (Google)
        String url = "https://www.google.com";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
        speak("Opening website");
    }

    private void sendEmail() {
        // Perform the action to send an email using the Gmail app
        String[] recipients = {"recipient@example.com"};
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Hello");
        intent.putExtra(Intent.EXTRA_TEXT, "Just wanted to say hi!");

        try {
            startActivity(intent);
            speak("Sending email");
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Gmail app not found on your device", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGoogleMaps() {
        // Perform the action to open Google Maps
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=Google");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
        speak("Opening Google Maps");
    }

    private void openGoogleContacts() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("content://contacts/people");
            intent.setData(uri);
            startActivity(intent);
            speak("Opening Google Contacts");
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Google Contacts app not found on your device", Toast.LENGTH_SHORT).show();
        }
    }

    private void speak(String text) {
        // Use the text-to-speech engine to speak the provided text
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Set the language for text-to-speech
            int result = tts.setLanguage(Locale.getDefault());

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(getApplicationContext(), "Text-to-speech language not supported on your device", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Failed to initialize text-to-speech engine", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        // Shutdown text-to-speech engine
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
