package ninja.genuine.conjure.mc;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTBase.NBTPrimitive;
import ninja.genuine.conjure.data.Data;
import ninja.genuine.conjure.data.DataException;
import ninja.genuine.conjure.data.collection.DataArray;
import ninja.genuine.conjure.data.collection.DataByteArray;
import ninja.genuine.conjure.data.collection.DataCompound;
import ninja.genuine.conjure.data.collection.DataIntegerArray;
import ninja.genuine.conjure.data.primitive.DataBoolean;
import ninja.genuine.conjure.data.primitive.DataByte;
import ninja.genuine.conjure.data.primitive.DataDouble;
import ninja.genuine.conjure.data.primitive.DataFloat;
import ninja.genuine.conjure.data.primitive.DataInteger;
import ninja.genuine.conjure.data.primitive.DataLong;
import ninja.genuine.conjure.data.primitive.DataNull;
import ninja.genuine.conjure.data.primitive.DataShort;
import ninja.genuine.conjure.data.primitive.DataString;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagEnd;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

public class MCToData {

	public static DataCompound create(final ItemStack stack) {
		return create(stack.writeToNBT(new NBTTagCompound()));
	}

	private static DataNull create(final NBTTagEnd nbt) {
		return DataNull.INSTANCE;
	}

	public static DataByte create(final NBTTagByte nbt) {
		return new DataByte(nbt.func_150290_f());
	}

	public static DataShort create(final NBTTagShort nbt) {
		return new DataShort(nbt.func_150289_e());
	}

	public static DataInteger create(final NBTTagInt nbt) {
		return new DataInteger(nbt.func_150287_d());
	}

	public static DataFloat create(final NBTTagFloat nbt) {
		return new DataFloat(nbt.func_150288_h());
	}

	public static DataDouble create(final NBTTagDouble nbt) {
		return new DataDouble(nbt.func_150286_g());
	}

	public static DataLong create(final NBTTagLong nbt) {
		return new DataLong(nbt.func_150291_c());
	}

	public static DataString create(final NBTTagString nbt) {
		return new DataString(nbt.func_150285_a_());
	}

	public static DataIntegerArray create(final NBTTagIntArray nbt) {
		return new DataIntegerArray(nbt.func_150302_c());
	}

	public static DataByteArray create(final NBTTagByteArray nbt) {
		return new DataByteArray(nbt.func_150292_c());
	}

	public static DataArray create(final NBTTagList nbt) {
		final DataArray list = new DataArray();
		final int count = nbt.tagCount();
		switch (nbt.func_150303_d()) {
			case 6:
				for (int i = 0; i < count; i++)
					list.add(new DataDouble(nbt.func_150309_d(i)));
				break;
			case 5:
				for (int i = 0; i < count; i++)
					list.add(new DataFloat(nbt.func_150308_e(i)));
				break;
			case 8:
				for (int i = 0; i < count; i++)
					list.add(new DataString(nbt.getStringTagAt(i)));
				break;
			case 10:
				for (int i = 0; i < count; i++)
					list.add(create(nbt.getCompoundTagAt(i)));
				break;
			case 11:
				for (int i = 0; i < count; i++)
					list.add(new DataIntegerArray(nbt.func_150306_c(i)));
				break;
		}
		return list;
	}

	public static DataCompound create(final NBTTagCompound nbt) {
		final DataCompound compound = new DataCompound();
		for (final Object key : nbt.func_150296_c())
			try {
				compound.set((String) key, create(nbt.getTag((String) key)));
			} catch (final Exception e) {}
		return compound;
	}

	public static DataBoolean create(final NBTPrimitive nbt) {
		return new DataBoolean(nbt.func_150290_f() != 0);
	}

	public static Data<?> create(final NBTBase nbt) throws Exception {
		if (nbt instanceof NBTTagEnd)
			return create((NBTTagEnd) nbt);
		if (nbt instanceof NBTTagByte)
			return create((NBTTagByte) nbt);
		if (nbt instanceof NBTTagShort)
			return create((NBTTagShort) nbt);
		if (nbt instanceof NBTTagInt)
			return create((NBTTagInt) nbt);
		if (nbt instanceof NBTTagFloat)
			return create((NBTTagFloat) nbt);
		if (nbt instanceof NBTTagDouble)
			return create((NBTTagDouble) nbt);
		if (nbt instanceof NBTTagLong)
			return create((NBTTagLong) nbt);
		if (nbt instanceof NBTTagString)
			return create((NBTTagString) nbt);
		if (nbt instanceof NBTTagByteArray)
			return create((NBTTagByteArray) nbt);
		if (nbt instanceof NBTTagIntArray)
			return create((NBTTagIntArray) nbt);
		if (nbt instanceof NBTTagList)
			return create((NBTTagList) nbt);
		if (nbt instanceof NBTTagCompound)
			return create((NBTTagCompound) nbt);
		throw new DataException("Could not create Data from " + nbt.getClass().getSimpleName(), null);
	}
}
