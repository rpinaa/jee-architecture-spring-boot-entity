package org.example.seed.service.impl;

import org.example.seed.catalog.IssuePriority;
import org.example.seed.catalog.IssueStatus;
import org.example.seed.domain.Issue;
import org.example.seed.event.issue.*;
import org.example.seed.repository.IssueRepository;
import org.example.seed.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Future;

@Service
public class IssueServiceImpl implements IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Override
    @Async
    @Cacheable(value = "issues")
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public Future<CatalogIssueEvent> requestAllIssues(final RequestAllIssueEvent requestAllIssueEvent) {

        final Pageable pageable = new PageRequest(requestAllIssueEvent.getNumberPage() - 1, requestAllIssueEvent.getRecordsPerPage(), Sort.Direction.DESC, "title");
        final Page<Issue> issues = this.issueRepository.findAll(pageable);

        return new AsyncResult<>(CatalogIssueEvent.builder().issues(issues.getContent()).total(issues.getTotalElements()).build());
    }

    @Override
    @Async
    @CacheEvict(value = "issues", allEntries = true)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Future<ResponseIssueEvent> createIssue(final CreateIssueEvent createIssueEvent) {

        createIssueEvent.getIssue().setStatus(IssueStatus.OPEN);

        if (createIssueEvent.getIssue().getPriority() == null) {
            createIssueEvent.getIssue().setPriority(IssuePriority.MEDIUM);
        }

        this.issueRepository.save(createIssueEvent.getIssue());

        return new AsyncResult<>(null);
    }

    @Override
    @Async
    @Cacheable(value = "issues")
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public Future<ResponseIssueEvent> requestIssue(final RequestIssueEvent requestIssueEvent) {

        Issue issue = this.issueRepository.findOne(requestIssueEvent.getId());

        return new AsyncResult<>(ResponseIssueEvent.builder().issue(issue).build());
    }

    @Override
    @Async
    @CacheEvict(value = "issues", allEntries = true)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Future<ResponseIssueEvent> updateIssue(final UpdateIssueEvent updateIssueEvent) {

        final Issue currentIssue = this.issueRepository.findOne(updateIssueEvent.getIssue().getId());

        currentIssue.setDescription(updateIssueEvent.getIssue().getDescription());
        currentIssue.setTitle(updateIssueEvent.getIssue().getTitle());
        currentIssue.setPriority(updateIssueEvent.getIssue().getPriority());
        currentIssue.setStatus(updateIssueEvent.getIssue().getStatus());
        currentIssue.setType(updateIssueEvent.getIssue().getType());

        this.issueRepository.save(currentIssue);

        return new AsyncResult<>(null);
    }

    @Override
    @CacheEvict(value = "issues", allEntries = true)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteIssue(final DeleteIssueEvent deleteIssueEvent) {

        this.issueRepository.delete(deleteIssueEvent.getId());
    }
}
