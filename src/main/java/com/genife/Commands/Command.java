package com.genife.Commands;

import com.genife.InfiniteSculk;
import com.genife.Managers.SculkManager;
import com.genife.Runnables.SculkRunnable;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

import static com.genife.Managers.ConfigManager.*;

public class Command implements CommandExecutor {
    private final InfiniteSculk instance = InfiniteSculk.getInstance();
    private final SculkManager sculkManager = InfiniteSculk.getInstance().getSculkManager();

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length != 0) {

            // проверка, кто отправил команду (игрок/другие источники)
            if (!(sender instanceof Player)) {
                sender.sendMessage(ONLY_IN_GAME_MESSAGE);
                return false;
            }

            // Проверка на права
            if (!sender.hasPermission("isculk.use")) {
                sender.sendMessage(NO_PERMISSION_MESSAGE);
                return false;
            }

            if (Objects.equals(args[0], "start")) {
                if (args.length < 2) {
                    sender.sendMessage(INCORRECT_TYPING_MESSAGE);
                    return false;
                }

                // Кол-во повторений катализатора в линии (с отступом X блоков)
                int lengthRepeatsCount;

                try {
                    lengthRepeatsCount = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    // Если кол-во - не число, то выводим ошибку
                    sender.sendMessage(INCORRECT_TYPING_MESSAGE);
                    return false;
                }

                new SculkRunnable(sender, lengthRepeatsCount).runTaskTimer(instance, 0L, 2L);
                sender.sendMessage(START_MESSAGE);
                return true;
            }

            if (Objects.equals(args[0], "stop")) {

                sculkManager.stopTasks();

                sender.sendMessage(STOP_MESSAGE);
                return true;
            }
        }
        sender.sendMessage(INCORRECT_TYPING_MESSAGE);
        return false;
    }
}