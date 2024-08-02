package bet.astral.messenger.v2;

import bet.astral.messenger.v2.component.*;
import bet.astral.messenger.v2.info.MessageInfo;
import bet.astral.messenger.v2.info.MessageInfoBuilder;
import bet.astral.messenger.v2.info.MultiMessageInfo;
import bet.astral.messenger.v2.locale.LanguageTable;
import bet.astral.messenger.v2.locale.source.LanguageSource;
import bet.astral.messenger.v2.permission.Permission;
import bet.astral.messenger.v2.placeholder.Placeholder;
import bet.astral.messenger.v2.placeholder.GlobalPlaceholderManager;
import bet.astral.messenger.v2.placeholder.hooks.PlaceholderHookManager;
import bet.astral.messenger.v2.receiver.Receiver;
import bet.astral.messenger.v2.task.IScheduler;
import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.messenger.v2.translation.TranslationKeyRegistry;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Function;

/**
 * Represents a messenger which translates and is the handler of placeholder parsing in messages.
 */
public interface Messenger extends Randomly, MessageSender {
	@NotNull
	static IScheduler getScheduler() {
		return DefaultScheduler.ASYNC_SCHEDULER;
	}
	/**
	 * Creates a new instance of a messenger.
	 * @param logger logger
	 * @return messenger
	 */
	static Messenger of(Logger logger){
//		return new MessengerImpl(logger);
		return null;
	}

	/**
	 * Sets the dafault locale for this messenger using language source.
	 * @param defaultLocale
	 */
	void setDefaultLocale(@NotNull LanguageSource defaultLocale);

	/**
	 * Returns the translation key registry.
	 * @return translation key registry
	 */
	@NotNull
	TranslationKeyRegistry getTranslationKeyRegistry();

	/**
	 * Returns the language table for this messenger.
	 * @param locale Locale to search the language tables for
	 * @return language table
	 */
	@Nullable
	LanguageTable getLanguageTable(@NotNull Locale locale);

	/**
	 * Returns the language table for this messenger. Using {@link #getLocale()}
	 * @return language table
	 */
	@NotNull
	LanguageTable getLanguageTable();

	/**
	 * Returns the component type registry.
	 * @return component type registry
	 */
	@NotNull
	default ComponentTypeRegistry getComponentTypeRegistry(){
		return ComponentType.GLOBAL_COMPONENT_TYPE_REGISTRY;
	}

	void setPlaceholderHookManager(@NotNull PlaceholderHookManager hookManager);
	@NotNull PlaceholderHookManager getPlaceholderHookManager();
	void setPlaceholderLoader(@NotNull GlobalPlaceholderManager loader);
	@NotNull GlobalPlaceholderManager getPlaceholderLoader();

	void registerLanguageTable(@NotNull Locale locale, @NotNull LanguageTable table);

	/**
	 * Loads given translation keys in all the registered language tables.
	 * @param translationKeys translations
	 */
	void loadTranslations(@NotNull List<TranslationKey> translationKeys);
	/**
	 * Loads given translation keys in all the registered language tables.
	 * @param translationKeys translations
	 */
	void loadTranslations(@NotNull TranslationKey[] translationKeys);
	/**
	 * Loads given translation keys in the given locale
	 * @param translationKeys translations
	 */
	void loadTranslations(@NotNull Locale locale, @NotNull List<TranslationKey> translationKeys);
	/**
	 * Loads given translation keys in the given locale
	 * @param translationKeys translations
	 */
	void loadTranslations(@NotNull Locale locale, @NotNull TranslationKey[] translationKeys);


	Component parsePlaceholders(@NotNull Receiver receiver, @NotNull Component component, @NotNull PlaceholderHookManager hookManager);

	Component parsePlaceholders(@NotNull Receiver receiver, @NotNull Component component, @NotNull Collection<? extends Placeholder> placeholders);

	/**
	 * Returns if the messenger tries to use the receiver locale before resorting to the default locale.
	 * @return true if try to use receiver's locale, else false
	 */
	boolean tryToUseReceiverLocale();

	/**
	 * Returns the default locale
	 * @return locale
	 */
	@NotNull
	Locale getLocale();

	/**
	 * Parses given message info. Does not use receiver to try to use placeholder hooks in the given message.
	 * @param messageInfo message info
	 * @return component if could parse, else null
	 */
	@Nullable
	default Component parseComponent(@NotNull MessageInfo messageInfo, @NotNull ComponentType componentType) {
		return parseComponent(messageInfo, componentType, getEmptyReceiver(), false);
	}

	/**
	 * Parses given message info and depending on if messenger info is used uses receiver to parse message
	 * @param messageInfo message info
	 * @param componentType the component type which to get from the base component
	 * @param receiver the receiver who this component part is parsed for
	 * @param useReceiverLocale should the receiver's locale be used
	 * @return component if could parse, else null
	 */
	@Nullable
	Component parseComponent(@NotNull MessageInfo messageInfo, @NotNull ComponentType componentType, @NotNull Receiver receiver, boolean useReceiverLocale);
	/**
	 * Parses given the translation key using given locale and placeholders.
	 * @param translationKey translation key
	 * @param locale locale
	 * @param placeholders placeholders
	 * @return component if could parse, else null
	 */
	@Nullable
	default Component parseComponent(@NotNull TranslationKey translationKey, @NotNull Locale locale, @NotNull ComponentType componentType, @NotNull Placeholder... placeholders) {
		return parseComponent(createMessage(translationKey).withLocale(locale).addPlaceholders(placeholders).create(), componentType);
	}

	/**
	 * Returns base component from given locale. Does not try to fall back to language before given locale.
	 * @param translationKey translation key for the base component
	 * @param locale locale
	 * @return this
	 */
	@Nullable
	default ComponentBase getBaseComponent(@NotNull TranslationKey translationKey, @NotNull Locale locale) {
		return getBaseComponent(translationKey,locale, false);
	}
	/**
	 * Returns base component from given locale.
	 * Depending on tryFallBack value tries to fall back to languages before given locale.
	 * @param translationKey translation key for the base component
	 * @param locale locale
	 * @param tryFallBack should the system try
	 *                       to find a message from fall back language tables if no language was found in the language table.
	 * @return this
	 */
	@Nullable
	ComponentBase getBaseComponent(@NotNull TranslationKey translationKey, @NotNull Locale locale, boolean tryFallBack);

	/**
	 * Parses given message info and depending on if messenger info is used uses receiver to parse message
	 * @param messageInfo message info
	 * @param componentType the component type which to get from the base component
	 * @param receiver the receiver who this component part is parsed for
	 * @param useReceiverLocale should the receiver's locale be used
	 * @return component if could parse, else null
	 */
	default @Nullable ParsedComponentPart parseComponentPart(@NotNull MessageInfo messageInfo, @NotNull ComponentType componentType, @NotNull Receiver receiver, boolean useReceiverLocale) {
		Component component = parseComponent(messageInfo, componentType, receiver, useReceiverLocale);
		if (component == null){
			return null;
		}
		ComponentBase base = getLanguageTable().getComponentFallBack(messageInfo.getTranslationKey());
		if (base == null || base.isDisabled()){
			return null;
		}
		return new ParsedComponentPart(Objects.requireNonNull(base.getParts()).get(componentType), component);
	}


	/**
	 * Sets the default message prefix for the messenger
	 * @param prefix prefix
	 */
	void setPrefix(@Nullable Component prefix);

	Messenger disablePrefixForNextParse();
	Messenger enablePrefixForNextParse();
	Messenger enablePrefix();
	Messenger disablePrefix();

	/**
	 * Returns the default message prefix for the messenger
	 * @return prefix, nullable
	 */
	@Nullable
	Component getPrefix();

	void send(@NotNull MessageInfo... messageInformation) throws ClassCastException;
	void send(@NotNull MessageInfoBuilder... messageInformation) throws ClassCastException;
	void send(@NotNull MultiMessageInfo... multiMessageInformation) throws ClassCastException;

	@NotNull
	MessageInfoBuilder createMessage(@NotNull TranslationKey translation);
	/**
	 * Returns the console and players of the server to broadcast messages to
	 * @return combined receiver
	 */
	default Receiver broadcast() {
		List<Receiver> receivers = new LinkedList<>(getPlayers());
		receivers.add(console());
		return Receiver.of(receivers);
	}

	/**
	 * Returns the console and players if the given permission returns true on given receiver
	 * @param permission permission
	 * @return combined receiver
	 */
	default Receiver broadcast(@NotNull Permission permission) {
		Collection<Receiver> receivers = new LinkedList<>();
		if (console().hasPermission(permission)){
			receivers.add(console());
		}
		receivers.addAll(getPlayers().stream().filter(player->player.hasPermission(permission)).toList());
		return Receiver.of(receivers);
	}

	/**
	 * Returns all online players as receivers.
	 * @return players
	 */
	List<Receiver> getPlayers();
	/**
	 * Returns the console receiver
	 * @return console receiver
	 */
	Receiver console();

	/**
	 * Returns an empty receiver to use in methods which require a receiver
	 * @return receiver
	 */
	default Receiver getEmptyReceiver(){
		return Receiver.empty();
	}

	/**
	 * Registers a new receiver converter to the messenger
	 * @param converter converter
	 */
	void registerReceiverConverter(Function<Object, Receiver> converter);

	/**
	 * Converts the given object to an object.
	 * Returns null if none of the converters could convert the object to a receiver
	 * @param object object to convert
	 * @return converter
	 */
	@Nullable
	Receiver convertReceiver(@NotNull Object object);

	/**
	 * Returns logger of this messenger
	 * @return logger
	 */
	Logger getLogger();

	/**
	 * Returns if the receiver of a message should receive a translation key, if no message component was found
	 * @return true, if sends translation key, else false
	 */
	boolean shouldSendTranslationKey();

	/**
	 * Makes the message handler send the translation key, to the message if no message is found.
	 * @param value true if send the translation key, else false
	 */
	void setSendTranslationKey(boolean value);
}
