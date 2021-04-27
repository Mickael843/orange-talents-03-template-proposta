package com.mikkaeru.proposal.validator;

import com.mikkaeru.proposal.dto.ProposalRequest;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

import static com.mikkaeru.utils.FieldEncryptor.decode;

public class ProposalGroupSequenceProvider implements DefaultGroupSequenceProvider<ProposalRequest> {

    @Override
    public List<Class<?>> getValidationGroups(ProposalRequest proposalRequest) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(ProposalRequest.class);

        if (proposalRequest != null && proposalRequest.getDocument() != null) {
            var documentSize = decode(proposalRequest.getDocument()).length();

            if (documentSize == 11) {
                groups.add(CpfGroup.class);
            } else {
                groups.add(CnpjGroup.class);
            }
        }

        return groups;
    }
}
