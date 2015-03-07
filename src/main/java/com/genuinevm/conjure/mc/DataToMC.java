package com.genuinevm.conjure.mc;

import java.util.Map.Entry;

import com.genuinevm.conjure.data.Data;
import com.genuinevm.conjure.data.DataException;
import com.genuinevm.conjure.data.Primitive;
import com.genuinevm.conjure.data.PrimitiveArray;
import com.genuinevm.conjure.data.collection.*;
import com.genuinevm.conjure.data.primitive.*;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;

public class DataToMC {

	public static ItemStack createItemStack(final DataCompound compound) {
		return ItemStack.loadItemStackFromNBT(create(compound));
	}

	private static NBTTagEnd create(final DataNull nbt) {
		return new NBTTagEnd();
	}

	public static NBTTagByte create(final DataBoolean nbt) {
		return new NBTTagByte(nbt.toByte());
	}

	public static NBTTagByte create(final DataByte nbt) {
		return new NBTTagByte(nbt.value());
	}

	public static NBTTagShort create(final DataShort nbt) {
		return new NBTTagShort(nbt.value());
	}

	public static NBTTagInt create(final DataInteger nbt) {
		return new NBTTagInt(nbt.value());
	}

	public static NBTTagFloat create(final DataFloat nbt) {
		return new NBTTagFloat(nbt.value());
	}

	public static NBTTagDouble create(final DataDouble nbt) {
		return new NBTTagDouble(nbt.value());
	}

	public static NBTTagLong create(final DataLong nbt) {
		return new NBTTagLong(nbt.value());
	}

	public static NBTTagString create(final DataString nbt) {
		return new NBTTagString(nbt.value());
	}

	public static NBTTagIntArray create(final DataIntegerArray nbt) {
		return new NBTTagIntArray(nbt.value());
	}

	public static NBTTagByteArray create(final DataByteArray nbt) {
		return new NBTTagByteArray(nbt.value());
	}

	public static NBTTagList create(final DataArray nbt) {
		final NBTTagList tagList = new NBTTagList();
		final int count = nbt.length();
		switch (nbt.getArrayType()) {
		case 6:
			for (int i = 0; i < count; i++)
				tagList.appendTag(new NBTTagDouble(((Primitive) nbt.get(i)).toDouble()));
			break;
		case 5:
			for (int i = 0; i < count; i++)
				tagList.appendTag(new NBTTagFloat(((Primitive) nbt.get(i)).toFloat()));
			break;
		case 8:
			for (int i = 0; i < count; i++)
				tagList.appendTag(new NBTTagString(nbt.get(i).toString()));
			break;
		case 10:
			for (int i = 0; i < count; i++)
				try {
					tagList.appendTag(create(nbt.get(i)));
				}
				catch (Exception e) {}
			break;
		case 11:
			for (int i = 0; i < count; i++)
				tagList.appendTag(new NBTTagIntArray(((PrimitiveArray) nbt.get(i)).toIntArray()));
			break;
		}
		return tagList;
	}

	public static NBTTagCompound create(final DataCompound compound) {
		final NBTTagCompound tag = new NBTTagCompound();
		for (final Entry<String, Data> key : compound.value().entrySet())
			try {
				tag.setTag(key.getKey(), create(key.getValue()));
			}
			catch (Exception e) {}
		return tag;
	}

	public static NBTBase create(final Data<?> data) throws Exception {
		if (data instanceof DataNull)
			return create((DataNull) data);
		if (data instanceof DataBoolean)
			return create((DataBoolean) data);
		if (data instanceof DataByte)
			return create((DataByte) data);
		if (data instanceof DataShort)
			return create((DataShort) data);
		if (data instanceof DataInteger)
			return create((DataInteger) data);
		if (data instanceof DataFloat)
			return create((DataFloat) data);
		if (data instanceof DataDouble)
			return create((DataDouble) data);
		if (data instanceof DataLong)
			return create((DataLong) data);
		if (data instanceof DataString)
			return create((DataString) data);
		if (data instanceof DataByteArray)
			return create((DataByteArray) data);
		if (data instanceof DataIntegerArray)
			return create((DataIntegerArray) data);
		if (data instanceof DataList)
			return create((DataList) data);
		if (data instanceof DataCompound)
			return create((DataCompound) data);
		throw new DataException("No existing translation strategy was found for " + data.getClass().getSimpleName(), data);
	}
}
