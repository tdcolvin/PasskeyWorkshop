import { WebAuthnCredential } from "@simplewebauthn/server";
import { Firestore, FieldValue } from "@google-cloud/firestore";
import { LoggedInUser } from "./example-server";

// Firestore client uses Application Default Credentials when run inside Google Cloud.
const db = new Firestore();

export async function getUserByUsername(username: string): Promise<LoggedInUser | undefined> {
    const snapshot = await db.collection("users").where("username", "==", username).limit(1).get();
    if (snapshot.empty) {
        return undefined;
    }

    const doc = snapshot.docs[0];
    return { id: doc.id, ... (doc.data() as Omit<LoggedInUser, 'id'>) };
}

export async function getUserById(userId: string): Promise<LoggedInUser | undefined> {
    const doc = await db.collection("users").doc(userId).get();
    if (!doc.exists) {
        return undefined;
    }

    return { id: userId, ... (doc.data() as Omit<LoggedInUser, 'id'>) };
}

export async function addUser(userId: string, username: string) {
    await db.collection("users").doc(userId).set({ username });
}

export async function addUserCredential(userId: string, credentials: WebAuthnCredential) {
    const user = await getUserById(userId);
    if (!user) {
        throw Error("No such user");
    }

    // Use arrayUnion to avoid needing to read/update the entire array manually.
    await db.collection("users").doc(userId).update({
        credentials: FieldValue.arrayUnion(credentials),
    });
}

