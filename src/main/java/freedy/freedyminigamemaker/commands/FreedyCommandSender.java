// 
// Decompiled by Procyon v0.5.36
// 

package freedy.freedyminigamemaker.commands;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

public class FreedyCommandSender implements ConsoleCommandSender
{
    public String output;

    public FreedyCommandSender() {
        this.output = "";
    }
    
    public void sendMessage(final String message) {
        this.output = message;
    }
    
    public void sendMessage(final String[] messages) {
        this.output = Arrays.toString(messages);
    }
    
    public void sendMessage(final UUID sender, final String message) {
    }
    
    public void sendMessage(final UUID sender, final String[] messages) {
    }
    
    public Server getServer() {
        return null;
    }
    
    public String getName() {
        return null;
    }
    
    public Spigot spigot() {
        return null;
    }
    
    public boolean isConversing() {
        return false;
    }
    
    public void acceptConversationInput(final String input) {
    }
    
    public boolean beginConversation(final Conversation conversation) {
        return false;
    }
    
    public void abandonConversation(final Conversation conversation) {
    }
    
    public void abandonConversation(final Conversation conversation, final ConversationAbandonedEvent details) {
    }
    
    public void sendRawMessage(final String message) {
    }
    
    public void sendRawMessage(final UUID sender, final String message) {
    }
    
    public boolean isPermissionSet(final String name) {
        return false;
    }
    
    public boolean isPermissionSet(final Permission perm) {
        return false;
    }
    
    public boolean hasPermission(final String name) {
        return true;
    }
    
    public boolean hasPermission(final Permission perm) {
        return true;
    }
    
    public PermissionAttachment addAttachment(final Plugin plugin, final String name, final boolean value) {
        return null;
    }
    
    public PermissionAttachment addAttachment(final Plugin plugin) {
        return null;
    }
    
    public PermissionAttachment addAttachment(final Plugin plugin, final String name, final boolean value, final int ticks) {
        return null;
    }
    
    public PermissionAttachment addAttachment(final Plugin plugin, final int ticks) {
        return null;
    }
    
    public void removeAttachment(final PermissionAttachment attachment) {
    }
    
    public void recalculatePermissions() {
    }
    
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return null;
    }
    
    public boolean isOp() {
        return false;
    }
    
    public void setOp(final boolean value) {
    }
}
