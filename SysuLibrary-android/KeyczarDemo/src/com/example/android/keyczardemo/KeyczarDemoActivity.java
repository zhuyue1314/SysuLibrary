/**
 * Copyright 2012 Kenny Root
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.keyczardemo;

import org.keyczar.Crypter;
import org.keyczar.exceptions.KeyczarException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class KeyczarDemoActivity extends Activity {
    private static final String TAG = "KeyczarDemo";

    static {
        FixBrokenCipherSpiProvider.insertIfNeeded();
    }

    private TextView mPlaintext;

    private TextView mCiphertext;

    private Crypter mCrypter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mPlaintext = (TextView) findViewById(R.id.plaintext);
        mCiphertext = (TextView) findViewById(R.id.ciphertext);

        final Button encrypt = (Button) findViewById(R.id.encrypt);
        encrypt.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                encryptMessage();
            }
        });

        final Button decrypt = (Button) findViewById(R.id.decrypt);
        decrypt.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                decryptMessage();
            }
        });

        try {
            mCrypter = new Crypter(new AndroidKeyczarReader(getResources(), "keys"));
        } catch (KeyczarException e) {
            mPlaintext.setText(R.string.problem);
            Log.d(TAG, "Couldn't load keyczar keys", e);
        }
    }

    protected void encryptMessage() {
        try {
            final String plaintext = mPlaintext.getText().toString();
            final String ciphertext = mCrypter.encrypt(plaintext);
            mCiphertext.setText(ciphertext);
        } catch (KeyczarException e) {
            Log.d(TAG, "Couldn't encrypt message", e);
            mCiphertext.setText(e.getMessage());
        }
    }

    protected void decryptMessage() {
        try {
            final String ciphertext = mCiphertext.getText().toString();
            final String plaintext = mCrypter.decrypt(ciphertext);
            mPlaintext.setText(plaintext);
        } catch (KeyczarException e) {
            Log.d(TAG, "Couldn't decrypt message", e);
            mCiphertext.setText(e.getMessage());
        }
    }
}
