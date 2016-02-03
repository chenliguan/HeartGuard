// Generated code from Butter Knife. Do not modify!
package cn.heart.main.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class AboutAppActivity$$ViewInjector<T extends cn.heart.main.activity.AboutAppActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034128, "field 'mGuideRelative' and method 'MeasureGuideClickListener'");
    target.mGuideRelative = finder.castView(view, 2131034128, "field 'mGuideRelative'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.MeasureGuideClickListener();
        }
      });
    view = finder.findRequiredView(source, 2131034130, "field 'mIntroRelative' and method 'ProjectIntroClickListener'");
    target.mIntroRelative = finder.castView(view, 2131034130, "field 'mIntroRelative'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.ProjectIntroClickListener();
        }
      });
    view = finder.findRequiredView(source, 2131034129, "field 'mContactRelative' and method 'ContactUsClickListener'");
    target.mContactRelative = finder.castView(view, 2131034129, "field 'mContactRelative'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.ContactUsClickListener();
        }
      });
  }

  @Override public void reset(T target) {
    target.mGuideRelative = null;
    target.mIntroRelative = null;
    target.mContactRelative = null;
  }
}
