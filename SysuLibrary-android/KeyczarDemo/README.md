Demo of Keyczar on Android
==========================

This is a demo application using Keyczar on Android. It does simple
encryption and decryption.

The main contribution of this code is a way to easily use keys embedded
in an APK's `assets` directory.

If you put your keys created with `KeyczarTool.jar` in `assets/keys/` then
the special `AndroidKeyczarReader` class can be used like this:

        Crypter crypter = new Crypter(new AndroidKeyczarReader(getResources(), "keys"));

Using Keyczar on pre-ICS devices
--------------------------------
This version also works around a bug present in Android versions before
ICS which causes problems with use of Keyczar.

For pre-ICS phones, you must include the `FixBrokenCipherSpiProvider`
class in your project.

In every class where you want to use Keyczar, you must insert this code
snippet:

        static {
            FixBrokenCipherSpiProvider.insertIfNeeded();
        }

Origin
------
This sprang from [my talk on Android and security][IOTalk] during Google
I/O 2012.

  [IOTalk]: http://youtu.be/RPJENzweI-A?t=32m41s
