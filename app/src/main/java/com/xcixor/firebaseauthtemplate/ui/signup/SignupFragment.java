package com.xcixor.firebaseauthtemplate.ui.signup;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.xcixor.firebaseauthtemplate.R;
import com.xcixor.firebaseauthtemplate.utils.ValidationUtil;

import static android.text.TextUtils.isEmpty;

public class SignupFragment extends Fragment implements View.OnClickListener, OnCompleteListener, OnFailureListener {

    private SignupViewModel mSignupViewModel;
    private ValidationUtil mValidationUtil;
    private View mRoot;
    private static final String TAG = "SignupFragment";
    private boolean isValidData = false;

//    widgets
    private TextInputEditText mEmail, mPassOne, mPassTwo;
    private TextInputLayout mEmailLayout, mPassOneLayout, mPassTwoLayout;
    private Button registerBtn;
    private RelativeLayout mProgressLayout;
    private ProgressBar mProgressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mSignupViewModel =
                ViewModelProviders.of(this).get(SignupViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_gallery, container, false);
        instantiateWidgets();
        registerBtn.setOnClickListener(this);
        return mRoot;
    }

    private void instantiateWidgets() {
        mEmail = mRoot.findViewById(R.id.email_input);
        mPassOne = mRoot.findViewById(R.id.pass_one_input);
        mPassTwo = mRoot.findViewById(R.id.pass_two_input);
        mEmailLayout = mRoot.findViewById(R.id.email_layout);
        mPassOneLayout = mRoot.findViewById(R.id.pass_one_layout);
        mPassTwoLayout = mRoot.findViewById(R.id.pass_two_layout);
        registerBtn = mRoot.findViewById(R.id.register_btn);
        mProgressLayout = mRoot.findViewById(R.id.progress_layout);
        mProgressBar = mRoot.findViewById(R.id.registration_progress_bar);
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        instantiateLogin();
    }

    /**
     * Called to create a toast message
     * @param message The message to display to user
     */
    private void makeToastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void instantiateLogin(){
        Log.d(TAG, "makeToastMessage: i'm clicked");
        if (!isEmpty(mEmail.getText().toString())
                && !isEmpty(mPassOne.getText().toString())
                && !isEmpty(mPassTwo.getText().toString())){
            validateData();
        }else{
            makeToastMessage("Please fill out all the fields");
        }
    }

    /**
     * Validates text inputs
     */
    private void validateData() {
        Log.d(TAG, "validateData: validating");
        if (!ValidationUtil.isEmail(mEmail.getText().toString())){
            mEmailLayout.setError("Invalid Email");
        }else{
            mEmailLayout.setError(null);
        }
        if (!ValidationUtil.isGreaterThanSixCharacters(mPassOne.getText().toString())){
            mPassOneLayout.setError("Password should be greater than six characters");
        }else {
            mPassOneLayout.setError(null);
            if (!ValidationUtil.textValuesMatch(mPassOne.getText().toString(), mPassTwo.getText().toString())) {
                mPassTwoLayout.setError("Passwords don't match");
            } else {
                mPassTwoLayout.setError(null);
                hideSoftKeyboard();
            }
        }

        registeruser();
    }

    /**
     * Hides phone keyboard
     */
    private void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager)getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().
                getCurrentFocus().
                getWindowToken(), 0);
    }

    /**
     * Called to create user with values entered
     */
    private void registeruser() {
        Log.d(TAG, "registerUser: registering");
        if (ValidationUtil.isEmail(mEmail.getText().toString())
                && ValidationUtil.isGreaterThanSixCharacters(mPassOne.getText().toString())
                && ValidationUtil.textValuesMatch(mPassOne.getText().toString(), mPassTwo.getText().toString())){
            registerNewEmail(mEmail.getText().toString(), mPassOne.getText().toString());
        }else{
            makeToastMessage("Please validate the data and try again");
        }
    }

    private void registerNewEmail(String email, String password) {
        showDialog();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this)
                .addOnFailureListener(this);
    }

    private void showDialog() {
        mProgressLayout.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        mProgressLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onComplete(@NonNull Task task) {
        Log.d(TAG, "onComplete: "+ task.isSuccessful());
        hideProgressBar();
        if (task.isSuccessful())
            Log.d(TAG, "onComplete: user is " + FirebaseAuth.getInstance().getCurrentUser().getUid());
            makeToastMessage("Registration Successful!");
            FirebaseAuth.getInstance().signOut();
            Navigation.findNavController(mRoot).navigate(R.id.nav_home);
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.d(TAG, "onFailure: exception");
        makeToastMessage(e.getMessage());
    }
}
