// Generated code from Butter Knife. Do not modify!
package cn.heart.main.controller;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class AppstartActivity$$ViewInjector<T extends cn.heart.main.controller.AppstartActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034140, "field 'mWelcomeImg'");
    target.mWelcomeImg = finder.castView(view, 2131034140, "field 'mWelcomeImg'");
  }

  @Override public void reset(T target) {
    target.mWelcomeImg = null;
  }
}
