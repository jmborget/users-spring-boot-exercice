package com.axpe.example.presentation.controllers.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.axpe.example.presentation.controllers.UsersController;
import com.axpe.example.presentation.http.CustomHeaders;
import com.axpe.example.service.ApiUsers;
import com.axpe.example.service.dto.UserDTO;
import com.axpe.example.service.exceptions.ContentNotFoundException;
import com.axpe.example.service.exceptions.ErrorCode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@WebMvcTest(controllers = UsersControllerImpl.class)
class UsersControllerTests {
    
    public static final String USER_LOGIN = "admin";
    public static final String USER_PASSW = "admin";
    
    public static final UUID HEADER_REQUEST_ID_1 = UUID.randomUUID();
    public static final String HEADER_LOCALE_ES = "es";
    
    public static final Long USER_ID_EXIST_1 = 1L;
    public static final Long USER_NOT_EXIST_EXIST_22 = 22L;
    
    private PodamFactory podamFactory = new PodamFactoryImpl();
    
    @Autowired
    private MockMvc mockMvc;
    
    //@Autowired
    private ObjectMapper objectMapper = JsonMapper.builder().disable(MapperFeature.USE_ANNOTATIONS)
            .build();
    
    @MockBean
    private ApiUsers apiUsers;
    
    UserDTO userDTO;
    UserDTO userDTO2;
    UserDTO userDTO3;
    UserDTO userDTO4;
    String jsonUserDTO = "{ \"firstName\": \"Pepe\", \"lastName\": \"González\", \"lastName2\": \"Pérez\", \"email\": \"pepe2@axpe.es\", \"phone\": \"+34689446123\", \"nif\": \"12345789F\", \"nickname\": \"adim\", \"password\": \"$2a$10$FRDQgZ83i4/E7Edw6cijIu6lRxiBv5GJu5wD8CiRWC19kYTJLMBRe\", \"statusUser\": \"ACTIVO\" }";
    
    @BeforeEach
    public void init() {
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("jborgex@gmail.com");
        userDTO.setFirstName("Jose");
        userDTO.setLastName("Borge");
        userDTO.setLastName2("Torres");
        userDTO.setNickname("josborg");
        userDTO.setPassword("1234");
        userDTO.setNif("123456789N");
        userDTO.setPhone("+34683317517");
        userDTO.setStatusUser("ACTIVO");
        userDTO.setCreatedAt(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
        userDTO.setUpdatedAt(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
        userDTO.setCanceledAt(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
        
        userDTO2 = userDTO.withId(2L);
        userDTO3 = userDTO.withId(3L);
        userDTO4 = userDTO.withId(4L);
        
        //        this.mockMvc = MockMvcBuilders.standaloneSetup(usersController)
        //                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
        
    }
    
    @Test
    void shouldReturnUserDTO() throws Exception {
        
        Mockito.when(apiUsers.getUserById(1L)).thenReturn(userDTO);
        mockMvc.perform(get(UsersControllerImpl.BASE_URL + "/{id}/", 1L))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockUser(username = USER_LOGIN, password = USER_PASSW, roles = "USER")
    void findById_when_exists_returnData() throws Exception {
        // given
        final String path = UsersController.BASE_URL + UsersController.URL_ID;
        
        UserDTO userDto = podamFactory.manufacturePojo(UserDTO.class);
        userDto.setPassword(null);
        when(apiUsers.getUserById(USER_ID_EXIST_1)).thenReturn(userDto);
        
        // when
        RequestBuilder requestBuilder = buildValidRequestBuilder(
                MockMvcRequestBuilders.get(path, USER_ID_EXIST_1.toString()));
        
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andDo(print()).andReturn();
        
        // then
        assertThat(
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserDTO.class))
                        .usingRecursiveComparison().isEqualTo(userDto).ignoringFields();
        
        verify(apiUsers, times(1)).getUserById(USER_ID_EXIST_1);
    }
    
    @Disabled // content not found
    @Test
    void shouldReturnContentNotFoundException() throws Exception {
        
        Mockito.when(apiUsers.getUserById(1L))
                .thenThrow(new ContentNotFoundException(ErrorCode.OBJECT_NOT_FOUND));
        mockMvc.perform(get(UsersControllerImpl.BASE_URL + "/{id}/", 1L))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void shouldReturnPage() throws Exception {
        
        Page<UserDTO> page = new PageImpl<UserDTO>(
                Arrays.asList(userDTO, userDTO2, userDTO3, userDTO4));
        
        Mockito.when(apiUsers.findByParams(Mockito.any())).thenReturn(page);
        mockMvc.perform(get(UsersControllerImpl.BASE_URL)).andExpect(status().isOk());
    }
    
    @Test
    void shouldSaveUserDTO() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(userDTO);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(json);
        System.out.println();
        System.out.println();
        Mockito.when(this.apiUsers.save(Mockito.any())).thenReturn(userDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.post(UsersControllerImpl.BASE_URL).content(jsonUserDTO)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
    
    @Test
    void shouldErrorMediaType() throws Exception {
        
        mockMvc.perform(post(UsersControllerImpl.BASE_URL))
                .andExpect(status().isUnsupportedMediaType());
    }
    
    @Test
    void shouldEditUserDTO() throws Exception {
        
        mockMvc.perform(MockMvcRequestBuilders.put(UsersControllerImpl.BASE_URL + "/{id}/", 1L)
                .content(jsonUserDTO).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
    }
    
    @Test
    void shouldDeleteUserDTO() throws Exception {
        
        mockMvc.perform(MockMvcRequestBuilders.delete(UsersControllerImpl.BASE_URL + "/{id}/", 1L))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void shouldPatchUserDTO() throws Exception {
        Mockito.when(this.apiUsers.patchUserById(Mockito.any(), Mockito.any())).thenReturn(userDTO);
        
        mockMvc.perform(MockMvcRequestBuilders.patch(UsersControllerImpl.BASE_URL + "/{id}/", 1L)
                .content("{ \"firstName\": \"Pepe\" }").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
    
    /**
     * Build a valid RequestBuilder with mandatory headers and authorization options
     * 
     * @param mockMvcRequestBuilder
     * @return
     */
    private MockHttpServletRequestBuilder buildValidRequestBuilder(
            MockHttpServletRequestBuilder mockMvcRequestBuilder) {
        return mockMvcRequestBuilder.header(CustomHeaders.X_REQUEST_ID, HEADER_REQUEST_ID_1)
                .header(HttpHeaders.ACCEPT_LANGUAGE, HEADER_LOCALE_ES)
                .accept(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(MediaType.APPLICATION_JSON).with(httpBasic(USER_LOGIN, USER_PASSW))
                .with(csrf());
    }
    
}
