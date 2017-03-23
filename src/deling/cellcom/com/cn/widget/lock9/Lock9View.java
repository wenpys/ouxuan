/*
 * Copyright 2015-2016 TakWolf
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package deling.cellcom.com.cn.widget.lock9;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import cellcom.com.cn.deling.R;
/**
 * 
 * @author zsw	���������view
 *
 */
public class Lock9View extends ViewGroup {

	/**
	 * �ڵ���ض���
	 */
	private List<Pair<NodeView, NodeView>> lineList = new ArrayList<Pair<NodeView, NodeView>>(); // �Ѿ����ߵĽڵ�����
	private NodeView currentNode; // ���һ�������Ľڵ㣬null��ʾ��û�е����κνڵ�
	private float x; // ��ǰ��ָ����x
	private float y; // ��ǰ��ָ����y

	/**
	 * �Զ��������б�
	 */
	private Drawable nodeSrc;
	private Drawable nodeOnSrc;
	private float nodeSize; // �ڵ��С�������Ϊ0��������ڱ߾�ͼ������
	private float nodeAreaExpand; // �Խڵ�Ĵ������������չ
	private int nodeOnAnim; // �ڵ����ʱ�Ķ���
	private float lineWidth;
	private float padding; // �ڱ߾�
	private float spacing; // �ڵ�������

	/**
	 * �𶯹�����
	 */
	private Vibrator vibrator;
	private boolean enableVibrate;
	private int vibrateTime;

	/**
	 * �����õĻ���
	 */
	private Paint paint;

	/**
	 * ���빹����
	 */
	private StringBuilder passwordBuilder = new StringBuilder();

	/**
	 * ����ص��������ӿ�
	 */
	private CallBack callBack;

	public interface CallBack {

		void onFinish(String password);

	}

	public void setCallBack(CallBack callBack) {
		this.callBack = callBack;
	}

	/**
	 * ���캯��
	 */

	public Lock9View(Context context) {
		super(context);
		initFromAttributes(context, null, 0);
	}

	public Lock9View(Context context, AttributeSet attrs) {
		super(context, attrs);
		initFromAttributes(context, attrs, 0);
	}

	public Lock9View(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initFromAttributes(context, attrs, defStyleAttr);
	}

	// @TargetApi(Build.VERSION_CODES.LOLLIPOP)
	// public Lock9View(Context context, AttributeSet attrs, int defStyleAttr,
	// int defStyleRes) {
	// super(context, attrs, defStyleAttr, defStyleRes);
	// initFromAttributes(context, attrs, defStyleAttr);
	// }

	/**
	 * ��ʼ��
	 */
	private void initFromAttributes(Context context, AttributeSet attrs,
			int defStyleAttr) {
		// ��ȡ���������
		final TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.Lock9View, defStyleAttr, 0);

		nodeSrc = a.getDrawable(R.styleable.Lock9View_lock9_nodeSrc);
		nodeOnSrc = a.getDrawable(R.styleable.Lock9View_lock9_nodeOnSrc);
		nodeSize = a.getDimension(R.styleable.Lock9View_lock9_nodeSize, 0);
		nodeAreaExpand = a.getDimension(
				R.styleable.Lock9View_lock9_nodeAreaExpand, 0);
		nodeOnAnim = a.getResourceId(R.styleable.Lock9View_lock9_nodeOnAnim, 0);
//		lineColor = a.getColor(R.styleable.Lock9View_lock9_lineColor,
//				Color.argb(0, 59, 168, 49));
		lineWidth = a.getDimension(R.styleable.Lock9View_lock9_lineWidth, 0);
		padding = a.getDimension(R.styleable.Lock9View_lock9_padding, 0);
		spacing = a.getDimension(R.styleable.Lock9View_lock9_spacing, 0);

		enableVibrate = a.getBoolean(R.styleable.Lock9View_lock9_enableVibrate,
				false);
		vibrateTime = a.getInt(R.styleable.Lock9View_lock9_vibrateTime, 20);

		a.recycle();

		// ��ʼ������
		if (enableVibrate) {
			vibrator = (Vibrator) context
					.getSystemService(Context.VIBRATOR_SERVICE);
		}

		// ��ʼ������
		paint = new Paint(Paint.DITHER_FLAG);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(lineWidth);
		paint.setColor((context.getResources().getColor(R.color.green)));
		paint.setAntiAlias(true); // �����

		// ����node
		for (int n = 0; n < 9; n++) {
			NodeView node = new NodeView(getContext(), n + 1);
			addView(node);
		}

		// ���FLAG������ onDraw() ������ã�ԭ���� ViewGroup Ĭ��͸����������Ҫ���� onDraw()
		setWillNotDraw(false);
	}

	/**
	 *�����ø߶ȵ��ڿ�� - �����д���֤
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int size = measureSize(widthMeasureSpec); // �������
		setMeasuredDimension(size, size);
	}

	/**
	 *  ��������
	 */
	private int measureSize(int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec); // �õ�ģʽ
		int specSize = MeasureSpec.getSize(measureSpec); // �õ��ߴ�
		switch (specMode) {
		case MeasureSpec.EXACTLY:
		case MeasureSpec.AT_MOST:
			return specSize;
		default:
			return 0;
		}
	}

	/**
	 * ���������node�Ĳ���
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		if (changed) {
			if (nodeSize > 0) { // �������nodeSizeֵ���򽫽ڵ�����ھŵȷ���������
				float areaWidth = (right - left) / 3;
				for (int n = 0; n < 9; n++) {
					NodeView node = (NodeView) getChildAt(n);
					// ��ȡ3*3����������
					int row = n / 3;
					int col = n % 3;
					// ����ʵ�ʵ�����
					int l = (int) (col * areaWidth + (areaWidth - nodeSize) / 2);
					int t = (int) (row * areaWidth + (areaWidth - nodeSize) / 2);
					int r = (int) (l + nodeSize);
					int b = (int) (t + nodeSize);
					node.layout(l, t, r, b);
				}
			} else { // �����շָ�߾಼�֣��ֶ�����ڵ��С
				float nodeSize = (right - left - padding * 2 - spacing * 2) / 3;
				for (int n = 0; n < 9; n++) {
					NodeView node = (NodeView) getChildAt(n);
					// ��ȡ3*3����������
					int row = n / 3;
					int col = n % 3;
					// ����ʵ�ʵ����꣬Ҫ�����ڱ߾�ͷָ�߾�
					int l = (int) (padding + col * (nodeSize + spacing));
					int t = (int) (padding + row * (nodeSize + spacing));
					int r = (int) (l + nodeSize);
					int b = (int) (t + nodeSize);
					node.layout(l, t, r, b);
				}
			}
		}
	}

	/**
	 * �����ﴦ������
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			x = event.getX(); // ����Ҫʵʱ��¼��ָ������
			y = event.getY();
			NodeView nodeAt = getNodeAt(x, y);
			if (currentNode == null) { // ֮ǰû�е�
				if (nodeAt != null) { // ��һ����
					currentNode = nodeAt;
					currentNode.setHighLighted(true);
					passwordBuilder.append(currentNode.getNum());
					invalidate(); // ֪ͨ�ػ�
				}
			} else { // ֮ǰ�е�-������ô����Ҫ�ػ�
				if (nodeAt != null && !nodeAt.isHighLighted()) { // ��ǰ�������µ�
					nodeAt.setHighLighted(true);
					Pair<NodeView, NodeView> pair = new Pair<NodeView, NodeView>(
							currentNode, nodeAt);
					lineList.add(pair);
					// ��ֵ��ǰ��node
					currentNode = nodeAt;
					passwordBuilder.append(currentNode.getNum());
				}
				invalidate(); // ֪ͨ�ػ�
			}
			break;
		case MotionEvent.ACTION_UP:
			if (passwordBuilder.length() > 0) { // �д�����
				// �ص����
				if (callBack != null) {
					callBack.onFinish(passwordBuilder.toString());
				}
				// ���״̬
				lineList.clear();
				currentNode = null;
				passwordBuilder.setLength(0);
				// �������
				for (int n = 0; n < getChildCount(); n++) {
					NodeView node = (NodeView) getChildAt(n);
					node.setHighLighted(false);
				}
				// ֪ͨ�ػ�
				invalidate();
			}
			break;
		}
		return true;
	}

	/**
	 * ϵͳ���ƻص�-��Ҫ��������
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// �Ȼ������е�����
		for (Pair<NodeView, NodeView> pair : lineList) {
			canvas.drawLine(pair.first.getCenterX(), pair.first.getCenterY(),
					pair.second.getCenterX(), pair.second.getCenterY(), paint);
		}
		// ����Ѿ��е����ĵ㣬���ڵ��������ָλ��֮���������
		if (currentNode != null) {
			canvas.drawLine(currentNode.getCenterX(), currentNode.getCenterY(),
					x, y, paint);
		}
	}

	/**
	 * ��ȡ����������Node������null��ʾ��ǰ��ָ������Node֮��
	 */
	private NodeView getNodeAt(float x, float y) {
		for (int n = 0; n < getChildCount(); n++) {
			NodeView node = (NodeView) getChildAt(n);
			if (!(x >= node.getLeft() - nodeAreaExpand && x < node.getRight()
					+ nodeAreaExpand)) {
				continue;
			}
			if (!(y >= node.getTop() - nodeAreaExpand && y < node.getBottom()
					+ nodeAreaExpand)) {
				continue;
			}
			return node;
		}
		return null;
	}

	/**
	 * �ڵ�������
	 */
	private class NodeView extends View {

		private int num;
		private boolean highLighted = false;

		@SuppressWarnings("deprecation")
		public NodeView(Context context, int num) {
			super(context);
			this.num = num;
			setBackgroundDrawable(nodeSrc);
		}

		public boolean isHighLighted() {
			return highLighted;
		}

		@SuppressWarnings("deprecation")
		public void setHighLighted(boolean highLighted) {
			if (this.highLighted != highLighted) {
				this.highLighted = highLighted;
				if (nodeOnSrc != null) { // û�����ø���ͼƬ�򲻱仯
					setBackgroundDrawable(highLighted ? nodeOnSrc : nodeSrc);
				}
				if (nodeOnAnim != 0) { // ���Ŷ���
					if (highLighted) {
						startAnimation(AnimationUtils.loadAnimation(
								getContext(), nodeOnAnim));
					} else {
						clearAnimation();
					}
				}
				if (enableVibrate) { // ��
					if (highLighted) {
						vibrator.vibrate(vibrateTime);
					}
				}
			}
		}

		public int getCenterX() {
			return (getLeft() + getRight()) / 2;
		}

		public int getCenterY() {
			return (getTop() + getBottom()) / 2;
		}

		public int getNum() {
			return num;
		}

	}

}
