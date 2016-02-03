// Generated code from Butter Knife. Do not modify!
package cn.heart.main.controller;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class UserDetailActivity$$ViewInjector<T extends cn.heart.main.controller.UserDetailActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034244, "field 'mUser'");
    target.mUser = finder.castView(view, 2131034244, "field 'mUser'");
    view = finder.findRequiredView(source, 2131034248, "field 'mLinearSView'");
    target.mLinearSView = finder.castView(view, 2131034248, "field 'mLinearSView'");
    view = finder.findRequiredView(source, 2131034242, "field 'mCircleHead'");
    target.mCircleHead = finder.castView(view, 2131034242, "field 'mCircleHead'");
    view = finder.findRequiredView(source, 2131034246, "field 'mNick'");
    target.mNick = finder.castView(view, 2131034246, "field 'mNick'");
    view = finder.findRequiredView(source, 2131034256, "field 'mDelete' and method 'DeleteFamilyClickListener'");
    target.mDelete = finder.castView(view, 2131034256, "field 'mDelete'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.DeleteFamilyClickListener(p0);
        }
      });
    view = finder.findRequiredView(source, 2131034249, "field 'mBirthRelative' and method 'Relative_birthdayClickListener'");
    target.mBirthRelative = finder.castView(view, 2131034249, "field 'mBirthRelative'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.Relative_birthdayClickListener(p0);
        }
      });
    view = finder.findRequiredView(source, 2131034254, "field 'mWeight'");
    target.mWeight = finder.castView(view, 2131034254, "field 'mWeight'");
    view = finder.findRequiredView(source, 2131034253, "field 'mWeightRelative' and method 'Relative_weightClickListener'");
    target.mWeightRelative = finder.castView(view, 2131034253, "field 'mWeightRelative'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.Relative_weightClickListener(p0);
        }
      });
    view = finder.findRequiredView(source, 2131034252, "field 'mHigh'");
    target.mHigh = finder.castView(view, 2131034252, "field 'mHigh'");
    view = finder.findRequiredView(source, 2131034247, "field 'mGenderRelative'");
    target.mGenderRelative = finder.castView(view, 2131034247, "field 'mGenderRelative'");
    view = finder.findRequiredView(source, 2131034245, "field 'mNickRelative' and method 'Relative_nickClickListener'");
    target.mNickRelative = finder.castView(view, 2131034245, "field 'mNickRelative'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.Relative_nickClickListener(p0);
        }
      });
    view = finder.findRequiredView(source, 2131034250, "field 'mBirth'");
    target.mBirth = finder.castView(view, 2131034250, "field 'mBirth'");
    view = finder.findRequiredView(source, 2131034251, "field 'mHighRelative' and method 'Relative_highClickListener'");
    target.mHighRelative = finder.castView(view, 2131034251, "field 'mHighRelative'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.Relative_highClickListener(p0);
        }
      });
    view = finder.findRequiredView(source, 2131034241, "field 'mHeadRelative' and method 'Relative_headClickListener'");
    target.mHeadRelative = finder.castView(view, 2131034241, "field 'mHeadRelative'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.Relative_headClickListener(p0);
        }
      });
  }

  @Override public void reset(T target) {
    target.mUser = null;
    target.mLinearSView = null;
    target.mCircleHead = null;
    target.mNick = null;
    target.mDelete = null;
    target.mBirthRelative = null;
    target.mWeight = null;
    target.mWeightRelative = null;
    target.mHigh = null;
    target.mGenderRelative = null;
    target.mNickRelative = null;
    target.mBirth = null;
    target.mHighRelative = null;
    target.mHeadRelative = null;
  }
}
