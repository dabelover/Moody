package com.example.moody;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserViewModel extends ViewModel {

    private MutableLiveData<User> user;

    public UserViewModel() {
        user = new MutableLiveData<>();
        loadUserData();
    }

    public LiveData<User> getUser() {
        return user;
    }

    private void loadUserData() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            user.setValue(new User(firebaseUser.getDisplayName(), firebaseUser.getEmail()));
        } else {
            // Manejar el caso donde no hay usuario autenticado
        }
    }

    public void logout() {
        // Aquí puedes implementar la lógica de cierre de sesión, si es necesario
    }
}
