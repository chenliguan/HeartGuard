// Generated code from Butter Knife. Do not modify!
package cn.heart.main.controller;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class RegisterActivity$$ViewInjector<T extends cn.heart.main.controller.RegisterActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034188, "field 'mHide' and method 'hideClickListener'");
    target.mHide = finder.castView(view, 2131034188, "field 'mHide'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.hideClickListener(p0);
        }
      });
    view = finder.findRequiredView(source, 2131034192, "field 'mRegister' and method 'registerClickListener'");
    target.mRegister = finder.castView(view, 2131034192, "field 'mRegister'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.registerClickListener(p0);
        }
      });
    view = finder.findRequiredView(source, 2131034190, "field 'mCodetext' and method 'getcodeClickListener'");
    target.mCodetext = finder.castView(view, 2131034190, "field 'mCodetext'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.getcodeClickListener(p0);
        }
      });
    view = finder.findRequiredView(source, 2131034191, "field 'mCode'");
    target.mCode = finder.castView(view, 2131034191, "field 'mCode'");
    view = finder.findRequiredView(source, 2131034186, "field 'mUser'");
    target.mUser = finder.castView(view, 2131034186, "field 'mUser'");
    view = finder.findRequiredView(source, 2131034189, "field 'mShow' and method 'showClickListener'");
    target.mShow = finder.castView(view, 2131034189, "field 'mShow'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.showClickListener(p0);
        }
      });
    view = finder.findRequiredView(source, 2131034187, "field 'mPass'");
    target.mPass = finder.castView(view, 2131034187, "field 'mPass'");
    view = finder.findRequiredView(source, 2131034185, "field 'mNick'");
    target.mNick = finder.castView(view, 2131034185, "field 'mNick'");
  }

  @Override public void reset(T target) {
    target.mHide = null;
    target.mRegister = null;
    target.mCodetext = null;
    target.mCode = null;
    target.mUser = null;
    target.mShow = null;
    target.mPass = null;
    target.mNick = null;
  }
}
