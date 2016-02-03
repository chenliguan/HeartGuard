// Generated code from Butter Knife. Do not modify!
package cn.heart.service.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class FamilyListAdapter$ItemViewCache$$ViewInjector<T extends cn.heart.service.adapter.FamilyListAdapter.ItemViewCache> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034272, "field 'mRelativeFamily'");
    target.mRelativeFamily = finder.castView(view, 2131034272, "field 'mRelativeFamily'");
    view = finder.findRequiredView(source, 2131034274, "field 'mNick'");
    target.mNick = finder.castView(view, 2131034274, "field 'mNick'");
    view = finder.findRequiredView(source, 2131034273, "field 'mImageHead'");
    target.mImageHead = finder.castView(view, 2131034273, "field 'mImageHead'");
    view = finder.findRequiredView(source, 2131034275, "field 'mImagview'");
    target.mImagview = finder.castView(view, 2131034275, "field 'mImagview'");
  }

  @Override public void reset(T target) {
    target.mRelativeFamily = null;
    target.mNick = null;
    target.mImageHead = null;
    target.mImagview = null;
  }
}
