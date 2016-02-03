// Generated code from Butter Knife. Do not modify!
package cn.heart.service.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class SwitchUsertAdapter$ItemViewCache$$ViewInjector<T extends cn.heart.service.adapter.SwitchUsertAdapter.ItemViewCache> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034289, "field 'mRelativeFamily'");
    target.mRelativeFamily = finder.castView(view, 2131034289, "field 'mRelativeFamily'");
    view = finder.findRequiredView(source, 2131034290, "field 'mImageHead'");
    target.mImageHead = finder.castView(view, 2131034290, "field 'mImageHead'");
    view = finder.findRequiredView(source, 2131034292, "field 'mImagview'");
    target.mImagview = finder.castView(view, 2131034292, "field 'mImagview'");
    view = finder.findRequiredView(source, 2131034291, "field 'mNick'");
    target.mNick = finder.castView(view, 2131034291, "field 'mNick'");
  }

  @Override public void reset(T target) {
    target.mRelativeFamily = null;
    target.mImageHead = null;
    target.mImagview = null;
    target.mNick = null;
  }
}
