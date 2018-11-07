package ddd.infrastructure;

import static ddd.infrastructure.TestComparators.fieldByField;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;
import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ddd.application.IssuesApplication;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ddd.domain.Issue;
import ddd.domain.IssueNumber;
import ddd.domain.IssueRepository;
import ddd.domain.ParticipantID;
import ddd.domain.ProductID;
import ddd.domain.ProductVersion;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = IssuesApplication.class)
public class JpaIssueRepositoryTest extends IssueRepositoryContractTest {

    @Autowired
    private EntityManager entityManager;

    @Before
    public void setUp() {

        repository = new JpaIssueRepository(entityManager){
            @Override
            public void store(Issue issue) {
                super.store(issue);
                entityManager.flush();
                entityManager.clear();
            }
        };
    }



}
