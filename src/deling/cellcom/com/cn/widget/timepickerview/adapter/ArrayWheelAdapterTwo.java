package deling.cellcom.com.cn.widget.timepickerview.adapter;


/**
 * The simple Array wheel adapter
 * @param <T> the element type
 */
public class ArrayWheelAdapterTwo<T> implements WheelAdapter {
	
	/** The default items length */
	public static final int DEFAULT_LENGTH = 4;
	
	// items
	private String[] items;
	// length
	private int length;

	/**
	 * Constructor
	 * @param items the items
	 * @param length the max items length
	 */
	public ArrayWheelAdapterTwo(String[] items, int length) {
		this.items = items;
		this.length = length;
	}
	
	/**
	 * Contructor
	 * @param items the items
	 */
	public ArrayWheelAdapterTwo(String[] items) {
		this(items, DEFAULT_LENGTH);
	}

	@Override
	public Object getItem(int index) {
		if (index >= 0 && index < items.length) {
			return items[index];
		}
		return "";
	}

	@Override
	public int getItemsCount() {
		return items.length;
	}

	@Override
	public int indexOf(Object o){
		int len=getItemsCount();
		for (int i = 0; i < len; i++) {
			if(items[i].equals(o)){
				return i;
			}
		}
		return -1;
	}

}
