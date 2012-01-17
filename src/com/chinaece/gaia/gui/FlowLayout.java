package com.chinaece.gaia.gui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {

	private final static int VIEW_MARGIN = 0;

	public FlowLayout(Context context) {
		super(context);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		System.err.println(width);
		System.err.println(height);
		for (int index = 0; index < getChildCount(); index++) {
			final View child = getChildAt(index);
			measureChild(child, 
					MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
					MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));
		}
	}

	@Override
	protected void onLayout(boolean arg0, int L, int T, int R, int B) {
		final int count = getChildCount();
		System.err.println(getChildCount());
		if(count % 2 !=0)
			throw new IllegalStateException("wrong fields size");
		int row = 0;
		int prevB = 0;
		for (int i = 0; i < count; i=i+2) {
			final View child1 = this.getChildAt(i);
			final View child2 = this.getChildAt(i+1);
			int W1 = child1.getMeasuredWidth();
			int H1 = child1.getMeasuredHeight();
			int W2 = child2.getMeasuredWidth();
			int H2 = child2.getMeasuredHeight();
			if((W1 + W2 + VIEW_MARGIN + VIEW_MARGIN) > R)
				;
			else{
				int maxH = H2>H1?H2:H1;
				int L1 = VIEW_MARGIN ;
				int lT = 0,lB = 0;
				for(int T1 = 0;T1<=maxH;T1++)
				{
					for(int B1 = maxH;B1>=0;B1--){
						if(Math.abs(T1-(B1-(child1.getMeasuredHeight()+T1)))>1)
						{
							T1+=1;
							B1-=1;
						}
						else 
						{
							lT = T1+prevB;
							lB = B1+prevB;
							break;
						}
					}
				}
				int R1 = L1 + child1.getMeasuredWidth();
//				System.err.println(W1 + "," + H1 + "," + L1 + "," + lT + "," + R1 + "," + lB);
				child1.layout(L1, lT, R1, lB);
				int L2 = R1 + VIEW_MARGIN;
				int R2 = L2 + child2.getMeasuredWidth();
				int rT = 0,rB = 0;
				for(int T2 = 0;T2<=maxH;T2++)
				{
					for(int B2 = maxH;B2>=0;B2--){
						
						if(Math.abs(T2-(B2-(child2.getMeasuredHeight()+T2)))>1)
						{
							T2+=1;
							B2-=1;
						}
						else 
						{
							rT = T2+prevB;
							rB = B2+prevB;
							break;
						}
					}
				}
//				System.err.println(W2 + "," + H2 + "," + L2 + "," + rT + "," + R2 + "," + rB);
				child2.layout(L2,rT, R2, rB);
				row++;
				prevB +=maxH;
			}
		}
	}
}
