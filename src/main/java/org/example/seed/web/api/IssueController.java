package org.example.seed.web.api;

import org.example.seed.domain.Issue;
import org.example.seed.group.MomentumGroup;
import org.example.seed.group.issue.IssueCreateGroup;
import org.example.seed.group.issue.IssueUpdateGroup;
import org.example.seed.service.IssueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "/issues")
public class IssueController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IssueService issueService;

    @Autowired
    private CounterService counterService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Callable<Page<Issue>> getAllIssues(@RequestParam("$numberPage") final Integer numberPage, @RequestParam("$recordsPerPage") final Integer recordsPerPage) throws ExecutionException, InterruptedException {
        this.logger.info("> getAllIssues");

        this.counterService.increment("services.issues.findAll.invoke");

        final Page<Issue> issues = this.issueService.findAll(numberPage - 1, recordsPerPage).get();

        this.logger.info("< getAllIssues");

        return () -> issues;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Callable<Issue> createIssue(@RequestBody @Validated(value = {IssueCreateGroup.class}) final Issue issue) {
        this.logger.info("> createIssue");

        final Issue currentIssue = this.issueService.create(issue);

        this.logger.info("< createIssue");

        return () -> currentIssue;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Callable<Issue> getIssue(@PathVariable("id") final String id) throws ExecutionException, InterruptedException {
        this.logger.info("> getIssue");

        final Issue currentIssue = this.issueService.find(id).get();

        this.logger.info("< getIssue");

        return () -> currentIssue;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Callable<Issue> updateIssue(@RequestBody @Validated(value = {MomentumGroup.class, IssueUpdateGroup.class}) final Issue issue) {
        this.logger.info("> updateIssue");

        final Issue currentIssue = this.issueService.update(issue);

        this.logger.info("< updateIssue");

        return () -> currentIssue;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIssue(@PathVariable("id") String issueId) {
        this.logger.info("> deleteIssue");

        this.issueService.delete(issueId);

        this.logger.info("< deleteIssue");
    }
}
