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

import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import android.os.Build;

/**
 * Fixes a problem in the CipherSpi provided in Android before Ice Cream
 * Sandwich. Whenever {@code null} is returned from {@code engineUpdate()} using
 * the {@code ByteBuffer} interface, it would cause a NullPointerException.
 */
public class FixBrokenCipherSpiProvider extends Provider {
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    private static class Holder {
        private static final FixBrokenCipherSpiProvider INSTANCE = new FixBrokenCipherSpiProvider();
        static {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                Security.insertProviderAt(INSTANCE, 1);
            }
        }
    }

    public static void insertIfNeeded() {
        Holder.INSTANCE.poke();
    }

    private void poke() {
    }

    public FixBrokenCipherSpiProvider() {
        super("FixBrokenCipherSpiProvider", 1.0, "Workaround for bug in pre-ICS Harmony");

        put("Cipher.AES", FixBrokenCipherSpi.AES.class.getName());
    }

    /**
     * Proxies a real Cipher provider to work around a bug in pre-ICS Harmony
     * that an {@code update()} would return {@code null}.
     */
    public static class FixBrokenCipherSpi extends CipherSpi {
        public static class AES extends FixBrokenCipherSpi {
            public AES() {
                super("AES");
            }
        }

        /** Cipher algorithm. */
        private String mAlgorithm;

        /** Cipher mode. */
        private String mMode;

        /** Cipher padding. */
        private String mPadding;

        /** The real cipher we're proxying. */
        private Cipher mInstance;

        public FixBrokenCipherSpi(String algorithm) {
            mAlgorithm = algorithm;
        }

        private Cipher getInstance() {
            if (mInstance != null) {
                return mInstance;
            }

            final String cipherAlg = "Cipher." + mAlgorithm;
            final String fullCipher;
            if (mMode != null && mPadding != null) {
                fullCipher = mAlgorithm + "/" + mMode + "/" + mPadding;
            } else {
                fullCipher = mAlgorithm;
            }

            final Provider[] providers = Security.getProviders(cipherAlg);
            for (Provider provider : providers) {
                if (provider instanceof FixBrokenCipherSpiProvider) {
                    continue;
                }

                try {
                    final Cipher instance = Cipher.getInstance(fullCipher, provider);
                    mInstance = instance;
                    return instance;
                } catch (GeneralSecurityException ignored) {
                    /*
                     * This shouldn't happen, but loop to find the next provider
                     * if it does.
                     */
                }
            }

            throw new RuntimeException("No other providers offer " + fullCipher);
        }

        @Override
        protected void engineSetMode(String mode) throws NoSuchAlgorithmException {
            mMode = mode;
        }

        @Override
        protected void engineSetPadding(String padding) throws NoSuchPaddingException {
            mPadding = padding;
        }

        @Override
        protected void engineInit(int opmode, Key key, SecureRandom random)
                throws InvalidKeyException {
            getInstance().init(opmode, key, random);
        }

        @Override
        protected void engineInit(int opmode, Key key, AlgorithmParameterSpec params,
                SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
            getInstance().init(opmode, key, params, random);
        }

        @Override
        protected void engineInit(int opmode, Key key, AlgorithmParameters params,
                SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
            getInstance().init(opmode, key, params, random);
        }

        @Override
        protected AlgorithmParameters engineGetParameters() {
            return getInstance().getParameters();
        }

        @Override
        protected byte[] engineGetIV() {
            return getInstance().getIV();
        }

        @Override
        protected int engineGetBlockSize() {
            return getInstance().getBlockSize();
        }

        @Override
        protected int engineGetOutputSize(int inputLen) {
            return getInstance().getOutputSize(inputLen);
        }

        @Override
        protected byte[] engineUpdate(byte[] input, int offset, int len) {
            final byte[] result = getInstance().update(input, offset, len);
            return result == null ? EMPTY_BYTE_ARRAY : result;
        }

        @Override
        protected int engineUpdate(byte[] input, int inputOffset, int inputLen, byte[] output,
                int outputOffset) throws ShortBufferException {
            return getInstance().update(input, inputOffset, inputLen, output, outputOffset);
        }

        @Override
        protected byte[] engineDoFinal(byte[] input, int inputOffset, int inputLen)
                throws IllegalBlockSizeException, BadPaddingException {
            return getInstance().doFinal(input == null ? EMPTY_BYTE_ARRAY : input, inputOffset,
                    inputLen);
        }

        @Override
        protected int engineDoFinal(byte[] input, int inputOffset, int inputLen, byte[] output,
                int outputOffset) throws ShortBufferException, IllegalBlockSizeException,
                BadPaddingException {
            return getInstance().doFinal(input == null ? EMPTY_BYTE_ARRAY : input, inputOffset,
                    inputLen, output, outputOffset);
        }
    }
}
