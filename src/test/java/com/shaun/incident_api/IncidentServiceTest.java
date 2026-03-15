package com.shaun.incident_api;

import com.shaun.incident_api.DTO.CreateIncidentRequest;
import com.shaun.incident_api.entity.Incident;
import com.shaun.incident_api.entity.IncidentStatus;
import com.shaun.incident_api.entity.Severity;
import com.shaun.incident_api.exception.ResourceNotFoundException;
import com.shaun.incident_api.metrics.IncidentMetrics;
import com.shaun.incident_api.repository.IncidentRepository;
import com.shaun.incident_api.service.IncidentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {

    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private IncidentMetrics incidentMetrics;

    private IncidentService incidentService;

    @BeforeEach
    void setUp() {
        incidentService = new IncidentService(incidentRepository, incidentMetrics);
    }

    // 测试：创建Incident成功
    @Test
    void create_ShouldCreateIncidentSuccessfully() {
        // Given: 准备测试数据
        CreateIncidentRequest request = new CreateIncidentRequest();
        request.setTitle("Test Incident");
        request.setSeverity(Severity.P2);

        Incident savedIncident = new Incident();
        savedIncident.setId(1L);
        savedIncident.setTitle("Test Incident");
        savedIncident.setSeverity(Severity.P2);
        savedIncident.setStatus(IncidentStatus.OPEN);
        savedIncident.setCreatedAt(LocalDateTime.now());

        when(incidentRepository.save(any(Incident.class))).thenReturn(savedIncident);

        // When: 执行测试方法
        Incident result = incidentService.create(request);

        // Then: 验证结果
        assertNotNull(result);
        assertEquals("Test Incident", result.getTitle());
        assertEquals(Severity.P2, result.getSeverity());
        assertEquals(IncidentStatus.OPEN, result.getStatus());
        verify(incidentRepository, times(1)).save(any(Incident.class));
    }

    // 测试：查询存在的Incident
    @Test
    void getById_WhenExists_ShouldReturnIncident() {
        Incident incident = new Incident();
        incident.setId(1L);
        incident.setTitle("Test");

        when(incidentRepository.findById(1L)).thenReturn(Optional.of(incident));

        Incident result = incidentService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test", result.getTitle());
    }

    // 测试：查询不存在的Incident抛异常
    @Test
    void getById_WhenNotExists_ShouldThrowException() {
        when(incidentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> incidentService.getById(999L));
    }

    // 测试：查询所有Incident
    @Test
    void getAll_ShouldReturnAllIncidents() {
        List<Incident> incidents = List.of(
                createIncident(1L, "Incident 1"),
                createIncident(2L, "Incident 2")
        );
        when(incidentRepository.findAll()).thenReturn(incidents);

        List<Incident> result = incidentService.getAll();

        assertEquals(2, result.size());
        assertEquals("Incident 1", result.get(0).getTitle());
    }

    // 测试：更新状态
    @Test
    void updateStatus_ShouldUpdateStatusSuccessfully() {
        Incident incident = new Incident();
        incident.setId(1L);
        incident.setStatus(IncidentStatus.OPEN);

        when(incidentRepository.findById(1L)).thenReturn(Optional.of(incident));
        when(incidentRepository.save(any(Incident.class))).thenReturn(incident);

        Incident result = incidentService.updateStatus(1L, IncidentStatus.CLOSED);

        assertEquals(IncidentStatus.CLOSED, result.getStatus());
    }

    // 辅助方法：创建测试数据
    private Incident createIncident(Long id, String title) {
        Incident incident = new Incident();
        incident.setId(id);
        incident.setTitle(title);
        incident.setSeverity(Severity.P2);
        incident.setStatus(IncidentStatus.OPEN);
        incident.setCreatedAt(LocalDateTime.now());
        return incident;
    }
}