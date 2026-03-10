import { Firestore } from "@google-cloud/firestore";

export interface SessionInfo {
    expectedChallenge?: string | null;
    requestedUsername?: string | null;
    precreatedUserId?: string | null;
    signInUsername?: string | null;
}

function verifySafeSessionId(sessionId: string) {
    if(!sessionId.match("^[0-9a-zA-Z_-]+$")) {
        throw Error("Bad session ID");
    }
}

const db = new Firestore();

export async function updateSession(sessionId: string, sessionInfo: SessionInfo) {
    verifySafeSessionId(sessionId);
    await db.collection("sessions").doc(sessionId).set(sessionInfo, { merge: true });
}

export async function getSession(sessionId: string): Promise<SessionInfo> {
    verifySafeSessionId(sessionId);
    const doc = await db.collection("sessions").doc(sessionId).get();
    return (doc.exists ? (doc.data() as SessionInfo) : {}) as SessionInfo;
}