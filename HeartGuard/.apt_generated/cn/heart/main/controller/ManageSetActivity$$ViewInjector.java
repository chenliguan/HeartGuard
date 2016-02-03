// Generated code from Butter Knife. Do not modify!
package cn.heart.main.controller;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class ManageSetActivity$$ViewInjector<T extends cn.heart.main.controller.ManageSetActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034152, "field 'mAccountSetting' and method 'AccountSettingClickListener'");
    target.mAccountSetting = finder.castView(view, 2131034152, "field 'mAccountSetting'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.AccountSettingClickListener(p0);
        }
      });
    view = finder.findRequiredView(source, 2131034150, "field 'mAddFamily' and method 'AddFamilyClickListener'");
    target.mAddFamily = finder.castView(view, 2131034150, "field 'mAddFamily'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.AddFamilyClickListener(p0);
        }
      });
    view = finder.findRequiredView(source, 2131034156, "field 'mAboutApp' and method 'AboutAppClickListener'");
    target.mAboutApp = finder.castView(view, 2131034156, "field 'mAboutApp'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.AboutAppClickListener(p0);
        }
      });
    view = finder.findRequiredView(source, 2131034144, "field 'mUserDetail' and method 'UserDetailClickListener'");
    target.mUserDetail = finder.castView(view, 2131034144, "field 'mUserDetail'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.UserDetailClickListener(p0);
        }
      });
    view = finder.findRequiredView(source, 2131034148, "field 'mFamilyGroup' and method 'FamilyGroupClickListener'");
    target.mFamilyGroup = finder.castView(view, 2131034148, "field 'mFamilyGroup'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.FamilyGroupClickListener(p0);
        }
      });
    view = finder.findRequiredView(source, 2131034147, "field 'mNick'");
    target.mNick = finder.castView(view, 2131034147, "field 'mNick'");
    view = finder.findRequiredView(source, 2131034154, "field 'mMyEcgSensor' and method 'MyEcgsensorClickListener'");
    target.mMyEcgSensor = finder.castView(view, 2131034154, "field 'mMyEcgSensor'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.MyEcgsensorClickListener(p0);
        }
      });
    view = finder.findRequiredView(source, 2131034145, "field 'mCircleHead'");
    target.mCircleHead = finder.castView(view, 2131034145, "field 'mCircleHead'");
  }

  @Override public void reset(T target) {
    target.mAccountSetting = null;
    target.mAddFamily = null;
    target.mAboutApp = null;
    target.mUserDetail = null;
    target.mFamilyGroup = null;
    target.mNick = null;
    target.mMyEcgSensor = null;
    target.mCircleHead = null;
  }
}
