// Generated code from Butter Knife. Do not modify!
package cn.heart.main.controller;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class AccountSetActivity$$ViewInjector<T extends cn.heart.main.controller.AccountSetActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034139, "field 'mOutLogin' and method 'OutLoginClickListener'");
    target.mOutLogin = finder.castView(view, 2131034139, "field 'mOutLogin'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.OutLoginClickListener(p0);
        }
      });
    view = finder.findRequiredView(source, 2131034132, "field 'mUser'");
    target.mUser = finder.castView(view, 2131034132, "field 'mUser'");
    view = finder.findRequiredView(source, 2131034137, "field 'mModifyPass' and method 'ModifyPasswordClickListener'");
    target.mModifyPass = finder.castView(view, 2131034137, "field 'mModifyPass'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.ModifyPasswordClickListener(p0);
        }
      });
  }

  @Override public void reset(T target) {
    target.mOutLogin = null;
    target.mUser = null;
    target.mModifyPass = null;
  }
}
