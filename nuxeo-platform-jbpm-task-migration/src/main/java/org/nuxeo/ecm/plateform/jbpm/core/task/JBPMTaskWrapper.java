/*
 * (C) Copyright 2012 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *    Nuxeo, Antoine Taillefer
 */
package org.nuxeo.ecm.plateform.jbpm.core.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.graph.exe.Comment;
import org.jbpm.taskmgmt.exe.PooledActor;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.platform.jbpm.JbpmService;
import org.nuxeo.ecm.platform.task.Task;
import org.nuxeo.ecm.platform.task.TaskComment;

/**
 * Simple wrapper around a Jbpm {@link TaskInstance}
 *
 *
 */
public class JBPMTaskWrapper implements Task {

    private static final long serialVersionUID = 1L;

    protected static Log log = LogFactory.getLog(JBPMTaskWrapper.class);

    public static final String JBPM_TASK_PROVIDER = "jbpmTaskProvider";

    private TaskInstance ti;

    private String directive;

    private String targetDocId;

    private String initiator;

    private Boolean validated;

    private DocumentModel doc;

    public JBPMTaskWrapper(TaskInstance ti) {
        this.ti = ti;
        targetDocId = (String) ti.getVariable(JbpmService.VariableName.documentId.name());
        directive = (String) ti.getVariable(JbpmService.TaskVariableName.directive.name());
        validated = Boolean.valueOf((String) ti.getVariable(JbpmService.TaskVariableName.validated.name()));
        initiator = (String) ti.getVariable(JbpmService.VariableName.initiator.name());
    }

    @Override
    public DocumentModel getDocument() {
        if (doc == null) {
            UnrestrictedSessionRunner runner = new UnrestrictedSessionRunner(
                    (String) ti.getVariable(JbpmService.VariableName.documentRepositoryName.name())) {
                @Override
                public void run() throws ClientException {
                    doc = session.getDocument(new IdRef(targetDocId));
                    doc.detach(true);
                }
            };
            try {
                runner.runUnrestricted();
            } catch (ClientException e) {
                log.error("Error while fetching DocumentModel", e);
            }
        }
        return doc;
    }

    @Override
    public String getId() {
        return String.valueOf(ti.getId());
    }

//    @Override
//    public String getTargetDocumentId() {
//        return targetDocId;
//    }

    @Override
    public List<String> getActors() throws ClientException {
        Set<PooledActor> pooledActors = ti.getPooledActors();
        List<String> actors = new ArrayList<String>(pooledActors.size());
        for (PooledActor pooledActor : pooledActors) {
            String actor = pooledActor.getActorId();
            if (actor.contains(":")) {
                actors.add(actor.split(":")[1]);
            } else {
                actors.add(actor);
            }
        }
        return actors;
    }

    @Override
    public String getInitiator() throws ClientException {
        return initiator;
    }

    @Override
    public String getName() throws ClientException {
        return ti.getName();
    }

    @Override
    public String getDescription() throws ClientException {
        return ti.getDescription();
    }

    @Override
    public String getDirective() throws ClientException {
        return directive;

    }

    @Override
    public List<TaskComment> getComments() throws ClientException {
        List<Comment> jbpmComments = ti.getComments();
        List<TaskComment> comments = new ArrayList<TaskComment>(
                jbpmComments.size());
        for (Comment taskComment : jbpmComments) {
            comments.add(new TaskComment(taskComment.getActorId(),
                    taskComment.getMessage(), taskComment.getTime()));
        }
        return comments;
    }

    @Override
    public String getVariable(String key) throws ClientException {
        if (Task.TASK_PROVIDER_KEY.equals(key)) {
            return JBPM_TASK_PROVIDER;
        }
        return (String) ti.getVariable(key);
    }

    @Override
    public Date getDueDate() throws ClientException {
        return ti.getDueDate();
    }

    @Override
    public Date getCreated() throws ClientException {
        return ti.getCreate();
    }

    @Override
    public Boolean isCancelled() throws ClientException {
        return ti.isCancelled();
    }

    @Override
    public Boolean isOpened() throws ClientException {
        return ti.isOpen();
    }

    @Override
    public Boolean hasEnded() throws ClientException {
        return ti.hasEnded();
    }

    @Override
    public Boolean isAccepted() throws ClientException {
        return validated;
    }

    @Override
    public Map<String, String> getVariables() throws ClientException {
        return ti.getVariables();
    }

    @Override
    public void setActors(List<String> actors) throws ClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setInitiator(String initiator) throws ClientException {
        throw new UnsupportedOperationException();
    }

//    @Override
//    public void setTargetDocumentId(String targetDocumentId) {
//        throw new UnsupportedOperationException();
//    }

    @Override
    public void setName(String name) throws ClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDescription(String description) throws ClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDirective(String directive) throws ClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setVariable(String key, String value) throws ClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDueDate(Date dueDate) throws ClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCreated(Date created) throws ClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAccepted(Boolean accepted) throws ClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setVariables(Map<String, String> variables)
            throws ClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addComment(String author, String text) throws ClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void cancel(CoreSession coreSession) throws ClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void end(CoreSession coreSession) throws ClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getDelegatedActors() {
        return null;
    }

    @Override
    public void setDelegatedActors(List<String> list) {

    }

    @Override
    public List<String> getTargetDocumentsIds() {
        return null;
    }

    @Override
    public void setTargetDocumentsIds(List<String> list) {

    }

    /**
     * @since 5.6
     */
    @Override
    public String getType() throws ClientException {
        return null;
    }

    /**
     * @since 5.6
     */
    @Override
    public void setType(String type) throws ClientException {
        // do nothing
    }

    /**
     * @since 5.6
     */
    @Override
    public String getProcessId() throws ClientException {
        return null;
    }

    @Override
    public String getProcessName() {
        return null;
    }

    /**
     * @since 5.6
     */
    @Override
    public void setProcessId(String processId) throws ClientException {
        // do nothing
    }

    @Override
    public void setProcessName(String s) {

    }
}
