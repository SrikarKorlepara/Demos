package com.stockstreaming.demo.service.impl;

import com.stockstreaming.demo.dto.DealerGroupCreateRequestDto;
import com.stockstreaming.demo.dto.DealerGroupRequestDto;
import com.stockstreaming.demo.dto.DealerGroupResponseDto;
import com.stockstreaming.demo.mapper.DealerGroupMapper;
import com.stockstreaming.demo.model.DealerGroup;
import com.stockstreaming.demo.repository.DealerGroupRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DealerGroupServiceImpl Unit Tests")
class DealerGroupServiceImplTest {

    @Mock
    private DealerGroupRepository dealerGroupRepository;

    @Mock
    private DealerGroupMapper dealerGroupMapper;

    @InjectMocks
    private DealerGroupServiceImpl dealerGroupService;

    // ----------------------------------------------------------------------
    // CREATE
    // ----------------------------------------------------------------------

    @Nested
    @DisplayName("Create Dealer Group")
    class CreateDealerGroupTests {

        @Test
        @DisplayName("should create dealer group successfully")
        void shouldCreateDealerGroupSuccessfully() {
            DealerGroupCreateRequestDto dto =
                    DealerGroupCreateRequestDto.builder()
                            .businessId("BUS123")
                            .name("Dealer Group")
                            .build();

            DealerGroup entity = DealerGroup.builder()
                    .businessId("BUS123")
                    .name("Dealer Group")
                    .build();

            DealerGroup saved = DealerGroup.builder()
                    .id(1L)
                    .businessId("BUS123")
                    .name("Dealer Group")
                    .build();

            DealerGroupResponseDto response =
                    DealerGroupResponseDto.builder()
                            .id(1L)
                            .businessId("BUS123")
                            .name("Dealer Group")
                            .build();

            when(dealerGroupRepository.existsByBusinessId("BUS123")).thenReturn(false);
            when(dealerGroupMapper.toCreateEntity(dto)).thenReturn(entity);
            when(dealerGroupRepository.save(entity)).thenReturn(saved);
            when(dealerGroupMapper.toResponseDto(saved)).thenReturn(response);

            DealerGroupResponseDto result =
                    dealerGroupService.createDealerGroup(dto);

            assertNotNull(result);
            assertEquals("Dealer Group", result.getName());

            verify(dealerGroupRepository).existsByBusinessId("BUS123");
            verify(dealerGroupRepository).save(entity);
        }

        @ParameterizedTest
        @ValueSource(strings = {"BUS123", "BUS456"})
        @DisplayName("should throw exception when businessId already exists")
        void shouldThrowWhenBusinessIdExists(String businessId) {
            DealerGroupCreateRequestDto dto =
                    DealerGroupCreateRequestDto.builder()
                            .businessId(businessId)
                            .name("Dealer Group")
                            .build();

            when(dealerGroupRepository.existsByBusinessId(businessId))
                    .thenReturn(true);

            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> dealerGroupService.createDealerGroup(dto)
            );

            assertEquals(
                    "Dealer Group with businessId " + businessId + " already exists.",
                    ex.getMessage()
            );

            verifyNoInteractions(dealerGroupMapper);
        }
    }

    // ----------------------------------------------------------------------
    // GET ALL
    // ----------------------------------------------------------------------

    @Test
    @DisplayName("should get all dealer groups")
    void shouldGetAllDealerGroups() {
        List<DealerGroup> entities =
                List.of(DealerGroup.builder().businessId("BUS1").build());

        List<DealerGroupResponseDto> responses =
                List.of(DealerGroupResponseDto.builder().businessId("BUS1").build());

        when(dealerGroupRepository.findAll()).thenReturn(entities);
        when(dealerGroupMapper.toResponseDtoList(entities)).thenReturn(responses);

        List<DealerGroupResponseDto> result =
                dealerGroupService.getAllDealerGroups();

        assertEquals(1, result.size());
        verify(dealerGroupRepository).findAll();
    }

    // ----------------------------------------------------------------------
    // GET BY BUSINESS ID
    // ----------------------------------------------------------------------

    @Nested
    @DisplayName("Get Dealer Group By BusinessId")
    class GetDealerGroupTests {

        @Test
        void shouldGetDealerGroupByBusinessId() {
            DealerGroup entity =
                    DealerGroup.builder().businessId("BUS123").build();

            DealerGroupResponseDto response =
                    DealerGroupResponseDto.builder().businessId("BUS123").build();

            when(dealerGroupRepository.findByBusinessId("BUS123"))
                    .thenReturn(Optional.of(entity));
            when(dealerGroupMapper.toResponseDto(entity))
                    .thenReturn(response);

            DealerGroupResponseDto result =
                    dealerGroupService.getDealerGroupByBusinessId("BUS123");

            assertNotNull(result);
            verify(dealerGroupRepository).findByBusinessId("BUS123");
        }

        @ParameterizedTest
        @ValueSource(strings = {"BUS1", "BUS2"})
        void shouldThrowWhenDealerGroupNotFound(String businessId) {
            when(dealerGroupRepository.findByBusinessId(anyString()))
                    .thenReturn(Optional.empty());

            assertThrows(
                    IllegalArgumentException.class,
                    () -> dealerGroupService.getDealerGroupByBusinessId(businessId)
            );
        }
    }

    // ----------------------------------------------------------------------
    // DELETE
    // ----------------------------------------------------------------------

    @Nested
    @DisplayName("Delete Dealer Group")
    class DeleteDealerGroupTests {


        @Test
        void shouldDeleteDealerGroupSuccessfully() {
            DealerGroup entity =
                    DealerGroup.builder().businessId("BUS123").build();

            when(dealerGroupRepository.findByBusinessId("BUS123"))
                    .thenReturn(Optional.of(entity));

            dealerGroupService.deleteDealerGroup("BUS123");

            verify(dealerGroupRepository).delete(entity);
        }

        @ParameterizedTest
        @ValueSource(strings = {"BUS1", "BUS2"})
        void shouldThrowWhenDeletingNonExistingDealerGroup(String businessId) {
            when(dealerGroupRepository.findByBusinessId(anyString()))
                    .thenReturn(Optional.empty());

            assertThrows(
                    IllegalArgumentException.class,
                    () -> dealerGroupService.deleteDealerGroup(businessId)
            );
        }
    }

    // ----------------------------------------------------------------------
    // UPDATE
    // ----------------------------------------------------------------------

    @Nested
    @DisplayName("Update Dealer Group")
    class UpdateDealerGroupTests {

        @ParameterizedTest
        @ValueSource(strings = {"BUS404", "BUS999"})
        void shouldThrowWhenUpdatingNonExistingDealerGroup(String businessId) {
            DealerGroupRequestDto dto =
                    DealerGroupRequestDto.builder().name("New").build();

            when(dealerGroupRepository.findByBusinessId(businessId))
                    .thenReturn(Optional.empty());

            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> dealerGroupService.updateDealerGroup(businessId, dto)
            );

            assertEquals(
                    "Dealer Group with businessId " + businessId + " does not exist.",
                    ex.getMessage()
            );

            verify(dealerGroupRepository).findByBusinessId(businessId);
            verifyNoInteractions(dealerGroupMapper);
            verify(dealerGroupRepository, never()).save(any());
        }


        @Test
        void shouldUpdateDealerGroupSuccessfully() {
            DealerGroup existing =
                    DealerGroup.builder()
                            .businessId("BUS123")
                            .name("Old")
                            .description("Old Desc")
                            .build();

            DealerGroupRequestDto dto =
                    DealerGroupRequestDto.builder()
                            .name("New")
                            .description("New Desc")
                            .build();

            when(dealerGroupRepository.findByBusinessId("BUS123"))
                    .thenReturn(Optional.of(existing));

            doAnswer(inv -> {
                DealerGroupRequestDto req = inv.getArgument(0);
                DealerGroup ent = inv.getArgument(1);
                ent.setName(req.getName());
                ent.setDescription(req.getDescription());
                return null;
            }).when(dealerGroupMapper).updateEntity(dto, existing);

            when(dealerGroupRepository.save(existing)).thenReturn(existing);

            dealerGroupService.updateDealerGroup("BUS123", dto);

            assertEquals("New", existing.getName());
            assertEquals("New Desc", existing.getDescription());
        }
    }

    // ----------------------------------------------------------------------
    // PARTIAL UPDATE
    // ----------------------------------------------------------------------

    @Nested
    @DisplayName("Partial Update Dealer Group")
    class PartialUpdateTests {

        @ParameterizedTest
        @ValueSource(strings = {"BUS404", "BUS999"})
        void shouldThrowWhenPartiallyUpdatingNonExistingDealerGroup(String businessId) {
            DealerGroupRequestDto dto =
                    DealerGroupRequestDto.builder().name("New").build();

            when(dealerGroupRepository.findByBusinessId(businessId))
                    .thenReturn(Optional.empty());

            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> dealerGroupService.partialUpdateDealerGroup(businessId, dto)
            );

            assertEquals(
                    "Dealer Group with businessId " + businessId + " does not exist.",
                    ex.getMessage()
            );

            verify(dealerGroupRepository).findByBusinessId(businessId);
            verifyNoInteractions(dealerGroupMapper);
            verify(dealerGroupRepository, never()).save(any());
        }


        @Test
        void shouldPartiallyUpdateDealerGroup() {
            DealerGroup existing =
                    DealerGroup.builder()
                            .businessId("BUS123")
                            .name("Old")
                            .description("Old Desc")
                            .build();

            DealerGroupRequestDto dto =
                    DealerGroupRequestDto.builder()
                            .name("New")
                            .build();

            when(dealerGroupRepository.findByBusinessId("BUS123"))
                    .thenReturn(Optional.of(existing));

            doAnswer(inv -> {
                DealerGroupRequestDto req = inv.getArgument(0);
                DealerGroup ent = inv.getArgument(1);
                if (req.getName() != null) ent.setName(req.getName());
                return null;
            }).when(dealerGroupMapper).partialUpdate(dto, existing);

            when(dealerGroupRepository.save(existing)).thenReturn(existing);

            dealerGroupService.partialUpdateDealerGroup("BUS123", dto);

            assertEquals("New", existing.getName());
            assertEquals("Old Desc", existing.getDescription());
        }
    }
}
