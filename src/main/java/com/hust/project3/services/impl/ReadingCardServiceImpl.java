package com.hust.project3.services.impl;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.readingCard.ReadingCardRequestDTO;
import com.hust.project3.entities.ReadingCard;
import com.hust.project3.entities.User;
import com.hust.project3.enums.ReadingCardStatusEnum;
import com.hust.project3.enums.ReadingCardTypeEnum;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.repositories.ReadingCardRepository;
import com.hust.project3.repositories.UserRepository;
import com.hust.project3.security.JwtTokenProvider;
import com.hust.project3.services.ReadingCardService;
import com.hust.project3.specification.ReadingCardSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadingCardServiceImpl implements ReadingCardService {
    private final ReadingCardRepository readingCardRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    public Page<ReadingCard> getReadingCardByUser(String jwt, ReadingCardRequestDTO dto, PagingRequestDTO pagingRequestDTO) throws NotFoundException {
        Pageable pageable = PageRequest.of(pagingRequestDTO.getPage(), pagingRequestDTO.getSize());
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        Specification<ReadingCard> spec = ReadingCardSpecification.byCriteria(dto, user);
        return readingCardRepository.findAll(spec, pageable);
    }

    @Override
    public ReadingCard getUserReadingCardById(String jwt, Long id) throws NotFoundException {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        List<ReadingCard> readingCardList = user.getReadingCardList();
        return readingCardList.stream()
                .filter(readingCard -> Objects.equals(readingCard.getId(), id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Reading card not found!"));
    }

    @Override
    @Transactional
    public ReadingCard addReadingCard(String jwt, ReadingCardRequestDTO dto) throws NotFoundException {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        ReadingCard readingCard = ReadingCard.builder()
                .code(UUID.randomUUID().toString().toUpperCase())
                .type(dto.getType())
                .startDate(LocalDate.now())
                .expiryDate(dto.getType() == ReadingCardTypeEnum.MONTHLY.ordinal() ? LocalDate.now().plusMonths(dto.getNumberOfPeriod()) : LocalDate.now().plusYears(dto.getNumberOfPeriod()))
                .status(ReadingCardStatusEnum.PENDING.ordinal())
                .updatedBy(email)
                .user(user)
                .build();
        return readingCardRepository.save(readingCard);
    }

    @Override
    public ReadingCard addPayment(String jwt, ReadingCardRequestDTO dto) {
        return null;
    }

    @Override
    public ReadingCard renewReadingCard(String jwt, ReadingCardRequestDTO dto) throws NotFoundException {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        List<ReadingCard> readingCardList = user.getReadingCardList();
        ReadingCard readingCard = readingCardList.stream()
                .filter(card -> Objects.equals(card.getId(), dto.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Reading card not found!"));

        if (readingCard.getType() == ReadingCardTypeEnum.MONTHLY.ordinal()) {
            readingCard.setExpiryDate(readingCard.getExpiryDate().plusMonths(dto.getNumberOfPeriod()));
        } else if (readingCard.getType() == ReadingCardTypeEnum.YEARLY.ordinal()) {
            readingCard.setExpiryDate(readingCard.getExpiryDate().plusYears(dto.getNumberOfPeriod()));
        }
        return readingCardRepository.save(readingCard);
    }

    @Override
    public ReadingCard cancelReadingCard(String jwt, ReadingCardRequestDTO dto) throws NotFoundException {
        String email = jwtTokenProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        List<ReadingCard> readingCardList = user.getReadingCardList();
        ReadingCard readingCard = readingCardList.stream()
                .filter(card -> Objects.equals(card.getId(), dto.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Reading card not found!"));
        readingCard.setStatus(ReadingCardStatusEnum.DEACTIVATED.ordinal());
        return readingCardRepository.save(readingCard);
    }
}
