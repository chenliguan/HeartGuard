// Generated code from Butter Knife. Do not modify!
package cn.heart.main.controller;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class LoginActivity$$ViewInjector<T extends cn.heart.main.controller.LoginActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034162, "field 'mUserName'");
    target.mUserName = finder.castView(view, 2131034162, "field 'mUserName'");
    view = finder.findRequiredView(source, 2131034163, "field 'mPassWord'");
    target.mPassWord = finder.castView(view, 2131034163, "field 'mPassWord'");
    view = finder.findRequiredView(source, 2131034170, "field 'mRegister' and method 'registerClickListener'");
    target.mRegister = finder.castView(view, 2131034170, "field 'mRegister'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.registerClickListener();
        }
      });
    view = finder.findRequiredView(source, 2131034166, "field 'mShow' and method 'showClickListener'");
    target.mShow = finder.castView(view, 2131034166, "field 'mShow'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.showClickListener();
        }
      });
    view = finder.findRequiredView(source, 2131034168, "field 'mLogin' and method 'LoginClickListener'");
    target.mLogin = finder.castView(view, 2131034168, "field 'mLogin'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.LoginClickListener();
        }
      });
    view = finder.findRequiredView(source, 2131034164, "field 'mHide' and method 'hideClickListener'");
    target.mHide = finder.castView(view, 2131034164, "field 'mHide'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.hideClickListener();
        }
      });
    view = finder.findRequiredView(source, 2131034169, "field 'mForget' and method 'forgetClickListener'");
    target.mForget = finder.castView(view, 2131034169, "field 'mForget'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.forgetClickListener(p0);
        }
      });
  }

  @Override public void reset(T target) {
    target.mUserName = null;
    target.mPassWord = null;
    target.mRegister = null;
    target.mShow = null;
    target.mLogin = null;
    target.mHide = null;
    target.mForget = null;
  }
}
