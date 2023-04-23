package com.genife.Commands;

import com.genife.InfiniteSculk;
import com.genife.Managers.SculkManager;
import com.genife.Runnables.SculkRunnable;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Command implements CommandExecutor {
    private static final String incorrect_typing = "§7[§eInfiniteSculk§7] §aНеправильный ввод: §6/isculk [start/stop] [кол-во (если start)]";
    private final InfiniteSculk instance = InfiniteSculk.getInstance();
    private final SculkManager sculkManager = InfiniteSculk.getInstance().getSculkManager();

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length != 0) {

            // проверка, кто отправил команду (игрок/другие источники)
            if (!(sender instanceof Player)) {
                sender.sendMessage("§7[§eInfiniteSculk§7] §fКоманду следует использовать в игре.");
                return false;
            }

            // проверка на права
            if (!sender.hasPermission("sculk.use")) {
                sender.sendMessage("§7[§eInfiniteSculk§7] §cУ тебя нет прав на использование этой команды!");
                return false;
            }

            if (Objects.equals(args[0], "start")) {
                if (args.length < 2) {
                    sender.sendMessage(incorrect_typing);
                    return false;
                }

                int count;

                try {
                    count = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    // если кол-во не число, то выводи ошибку
                    sender.sendMessage(incorrect_typing);
                    return false;
                }

                new SculkRunnable(sender, count).runTaskTimer(instance, 0L, 2L);
                sender.sendMessage("§7[§eInfiniteSculk§7] §7Распространение успешно запущено!");
                return true;
            }

            if (Objects.equals(args[0], "stop")) {

                sculkManager.stopTasks();

                sender.sendMessage("§7[§eInfiniteSculk§7] §7Все распространения завершены!");
                return true;
            }
        }

        sender.sendMessage(incorrect_typing);
        return false;
    }
}