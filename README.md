# Android & passkeys workshop

This workshop is in two parts. You can do either them in either order, and at each
stage you'll produce a working product that you can test.

## Part 1: The Android app
Here you will produce an Android app which authenticates, using passkeys, to the test
domain [auth.tomcolvin.co.uk](https://auth.tomcolvin.co.uk).

Follow the instructions in the [Android readme](./README-android.md).


## Part 2: The back end
Here you will produce a back end service based on SimpleWebAuthn which allows a front
end to authenticate with a passkey.
You will deploy this using Cloud Run.

Follow the instructions in the [Back end readme](./README-backend.md).


## Bonus round: Joining it all up
Once you've got the back end and the Android app both working, try to connect them both
up! Then your app will be authenticating to your own service rather than the demo
[auth.tomcolvin.co.uk](https://auth.tomcolvin.co.uk).


### Thanks for participating!
I hope you've had fun and maybe learned something!
If you need any help you can [find me on LinkedIn](https://linkedin.com/in/tdcolvin) or
contact me via my website [tomcolvin.co.uk](https://tomcolvin.co.uk).