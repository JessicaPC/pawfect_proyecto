// Generated by view binder compiler. Do not edit!
package com.example.pawfect.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.airbnb.lottie.LottieAnimationView;
import com.example.pawfect.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRecoverPasswordBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextInputEditText editEmail;

  @NonNull
  public final LottieAnimationView emailSentAnimation;

  @NonNull
  public final TextView emailTitle;

  @NonNull
  public final TextInputLayout inputEmail;

  @NonNull
  public final LottieAnimationView pendingEmailAnimation;

  @NonNull
  public final Button sendChangesButton;

  @NonNull
  public final Toolbar toolbar;

  private ActivityRecoverPasswordBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextInputEditText editEmail, @NonNull LottieAnimationView emailSentAnimation,
      @NonNull TextView emailTitle, @NonNull TextInputLayout inputEmail,
      @NonNull LottieAnimationView pendingEmailAnimation, @NonNull Button sendChangesButton,
      @NonNull Toolbar toolbar) {
    this.rootView = rootView;
    this.editEmail = editEmail;
    this.emailSentAnimation = emailSentAnimation;
    this.emailTitle = emailTitle;
    this.inputEmail = inputEmail;
    this.pendingEmailAnimation = pendingEmailAnimation;
    this.sendChangesButton = sendChangesButton;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRecoverPasswordBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRecoverPasswordBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_recover_password, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRecoverPasswordBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.edit_email;
      TextInputEditText editEmail = ViewBindings.findChildViewById(rootView, id);
      if (editEmail == null) {
        break missingId;
      }

      id = R.id.email_sent_animation;
      LottieAnimationView emailSentAnimation = ViewBindings.findChildViewById(rootView, id);
      if (emailSentAnimation == null) {
        break missingId;
      }

      id = R.id.email_title;
      TextView emailTitle = ViewBindings.findChildViewById(rootView, id);
      if (emailTitle == null) {
        break missingId;
      }

      id = R.id.input_email;
      TextInputLayout inputEmail = ViewBindings.findChildViewById(rootView, id);
      if (inputEmail == null) {
        break missingId;
      }

      id = R.id.pending_email_animation;
      LottieAnimationView pendingEmailAnimation = ViewBindings.findChildViewById(rootView, id);
      if (pendingEmailAnimation == null) {
        break missingId;
      }

      id = R.id.send_changes_button;
      Button sendChangesButton = ViewBindings.findChildViewById(rootView, id);
      if (sendChangesButton == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      return new ActivityRecoverPasswordBinding((ConstraintLayout) rootView, editEmail,
          emailSentAnimation, emailTitle, inputEmail, pendingEmailAnimation, sendChangesButton,
          toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
