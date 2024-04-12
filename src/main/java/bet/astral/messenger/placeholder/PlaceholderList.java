package bet.astral.messenger.placeholder;

import bet.astral.messenger.utils.PlaceholderUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class PlaceholderList extends LinkedList<Placeholder> {
	public PlaceholderList() {
	}

	public PlaceholderList(@NotNull Collection<? extends Placeholder> c) {
		super(c);
	}

	public static PlaceholderList combine(@NotNull Collection<Placeholder> placeholders1, @NotNull Collection<Placeholder>... placeholders2) {
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.addAll(placeholders1);
		for (Collection<Placeholder> placeholderCollection : placeholders2){
			placeholders.addAll(placeholderCollection);
		}
		return placeholders;
	}
	public static PlaceholderList combine(@NotNull Collection<Placeholder> placeholders1, @NotNull Placeholder... placeholders2){
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.addAll(placeholders1);
		placeholders.addAll(placeholders2);
		return placeholders;
	}
	public static PlaceholderList combine(@NotNull Placeholder[] placeholders1, @NotNull Collection<Placeholder>... placeholders2){
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.addAll(placeholders1);
		for (Collection<Placeholder> placeholderCollection : placeholders2){
			placeholders.addAll(placeholderCollection);
		}
		return placeholders;
	}
	public static PlaceholderList combine(@NotNull Placeholder[] placeholders1, @NotNull Placeholder[]... placeholders2){
		PlaceholderList placeholders = new PlaceholderList();
		placeholders.addAll(placeholders1);
		for (Placeholder[] placeholderCollection : placeholders2){
			placeholders.addAll(placeholderCollection);
		}
		return placeholders;
	}

	public boolean add(String name, Object info) {
		return add(PlaceholderUtils.createPlaceholder(null, name, info));
	}
	public void add(int index, String name, Object info) {
		add(index, PlaceholderUtils.createPlaceholder(null, name, index));
	}

	public void addAll(Placeholder... placeholders){
		this.addAll(Arrays.asList(placeholders));
	}
	public void addAll(String prefix, Placeholderable... placeholderables){
		this.addAll(prefix, Arrays.asList(placeholderables));
	}
	public void addAll(String prefix, List<Placeholderable> placeholderables){
		for (Placeholderable placeholderable : placeholderables){
			this.addAll(placeholderable.asPlaceholder(prefix));
		}
	}

	public void add(String prefix, Placeholderable placeholderable){
		this.addAll(placeholderable.asPlaceholder(prefix));
	}
}
