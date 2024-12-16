package com.mitu.bloctemplate

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup

public class GroupAction : DefaultActionGroup() {
    override fun update(e: AnActionEvent) {
        super.update(e)
    }

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        return super.getChildren(e)
    }
}