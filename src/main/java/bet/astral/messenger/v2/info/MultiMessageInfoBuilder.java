package bet.astral.messenger.v2.info;

import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderCollection;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MultiMessageInfoBuilder {
	private final ArrayList<MessageInfo> messages = new ArrayList<>();
	private List<Placeholder> placeholders = new LinkedList<>();

	public MultiMessageInfoBuilder and(@NotNull MessageInfo messageInfo){
		messages.add(messageInfo);
		return this;
	}
	public MultiMessageInfoBuilder and(@NotNull MessageInfoBuilder messageInfoBuilder){
		messages.add(messageInfoBuilder.create());
		return this;
	}
	public MultiMessageInfoBuilder setPlaceholders(List<? extends Placeholder> placeholders) {
		this.placeholders = new LinkedList<>(placeholders);
		return this;
	}

	public MultiMessageInfo create() {
		return new MultiMessageInfoImpl(messages, PlaceholderCollection.map(placeholders));
	}

	public void send(@NotNull Messenger messenger){
		messenger.send(this.create());
	}
}