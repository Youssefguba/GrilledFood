package com.example.grilledfood.Service;

import com.example.grilledfood.Common.Common;
import com.example.grilledfood.Model.Token;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String tokenRefreshed = FirebaseInstanceId.getInstance().getToken();
        if (Common.mCurrentUser != null)
            updateTokenToFirebase(tokenRefreshed);
    }

    private void updateTokenToFirebase(String tokenRefreshed) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tokenRef = database.getReference("Tokens");
        Token token = new Token(tokenRefreshed, false); //true because this token sent from client App..
        tokenRef.child(Common.mCurrentUser.getPhone()).setValue(token);
    }
}
