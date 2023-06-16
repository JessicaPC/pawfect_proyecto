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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.pawfect.R;
import com.google.android.material.imageview.ShapeableImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentProfileBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button editProfileButton;

  @NonNull
  public final TextView emailText;

  @NonNull
  public final TextView locationText;

  @NonNull
  public final TextView nameText;

  @NonNull
  public final TextView phoneText;

  @NonNull
  public final ShapeableImageView profileImg;

  @NonNull
  public final RecyclerView recyclerAnimals;

  @NonNull
  public final TextView textPublications;

  @NonNull
  public final Toolbar toolbar;

  private FragmentProfileBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button editProfileButton, @NonNull TextView emailText,
      @NonNull TextView locationText, @NonNull TextView nameText, @NonNull TextView phoneText,
      @NonNull ShapeableImageView profileImg, @NonNull RecyclerView recyclerAnimals,
      @NonNull TextView textPublications, @NonNull Toolbar toolbar) {
    this.rootView = rootView;
    this.editProfileButton = editProfileButton;
    this.emailText = emailText;
    this.locationText = locationText;
    this.nameText = nameText;
    this.phoneText = phoneText;
    this.profileImg = profileImg;
    this.recyclerAnimals = recyclerAnimals;
    this.textPublications = textPublications;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentProfileBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentProfileBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_profile, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentProfileBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.edit_profile_button;
      Button editProfileButton = ViewBindings.findChildViewById(rootView, id);
      if (editProfileButton == null) {
        break missingId;
      }

      id = R.id.email_text;
      TextView emailText = ViewBindings.findChildViewById(rootView, id);
      if (emailText == null) {
        break missingId;
      }

      id = R.id.location_text;
      TextView locationText = ViewBindings.findChildViewById(rootView, id);
      if (locationText == null) {
        break missingId;
      }

      id = R.id.name_text;
      TextView nameText = ViewBindings.findChildViewById(rootView, id);
      if (nameText == null) {
        break missingId;
      }

      id = R.id.phone_text;
      TextView phoneText = ViewBindings.findChildViewById(rootView, id);
      if (phoneText == null) {
        break missingId;
      }

      id = R.id.profile_img;
      ShapeableImageView profileImg = ViewBindings.findChildViewById(rootView, id);
      if (profileImg == null) {
        break missingId;
      }

      id = R.id.recycler_animals;
      RecyclerView recyclerAnimals = ViewBindings.findChildViewById(rootView, id);
      if (recyclerAnimals == null) {
        break missingId;
      }

      id = R.id.text_publications;
      TextView textPublications = ViewBindings.findChildViewById(rootView, id);
      if (textPublications == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      return new FragmentProfileBinding((ConstraintLayout) rootView, editProfileButton, emailText,
          locationText, nameText, phoneText, profileImg, recyclerAnimals, textPublications,
          toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
