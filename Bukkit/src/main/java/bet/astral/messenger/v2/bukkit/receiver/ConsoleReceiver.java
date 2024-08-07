package bet.astral.messenger.v2.bukkit.receiver;

import bet.astral.messenger.v2.bukkit.BukkitPlatform;
import bet.astral.messenger.v2.bukkit.scheduler.GlobalASyncScheduler;
import bet.astral.messenger.v2.permission.Permission;
import bet.astral.messenger.v2.receiver.Receiver;
import bet.astral.messenger.v2.task.IScheduler;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class ConsoleReceiver implements Receiver, ForwardingAudience {
	public static ConsoleReceiver CONSOLE_RECEIVER = new ConsoleReceiver();
	@Override
	public @NotNull IScheduler getScheduler() {
		return GlobalASyncScheduler.SCHEDULER;
	}

	@Override
	public @NotNull Locale getLocale() {
		return Locale.US;
	}

	@Override
	public boolean hasPermission(@NotNull Permission permission) {
		return permission.test(this);
	}

	@Override
	public boolean hasPermission(@NotNull String s) {
		return Bukkit.getConsoleSender().hasPermission(s);
	}

	@Override
	public @NotNull Iterable<? extends Audience> audiences() {
		return List.of(((BukkitPlatform) BukkitPlatform.getPlatform()).toReceiver(Bukkit.getConsoleSender()));
	}
}
