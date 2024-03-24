package com.java.aws.apisqssql.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.aws.apisqssql.model.Book;
import com.java.aws.apisqssql.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.IOException;

@Service
public class BookService {

    private static final Logger LOG = LoggerFactory.getLogger(BookService.class);

    private ObjectMapper objectMapper;

    private Book book;

    private BookRepository bookRepository;

    @Autowired
    public BookService(ObjectMapper objectMapper, BookRepository bookRepository){
        this.objectMapper = objectMapper;
        this.bookRepository = bookRepository;
    }

    @JmsListener(destination = "${aws.sqs.queue.product.events.name}")
    public void receiveProductEvent(TextMessage textMessage)
            throws JMSException, IOException {
        try{

            LOG.info("Mensagem Recebida: {}", textMessage.getText());
            Book book = objectMapper.readValue(textMessage.getText(), Book.class);
            LOG.info("Classe preenchida após o Parse do Json: {}", book.toString());
            //Book book = new Book(bookDto.getName(), bookDto.getAuthor());
            bookRepository.save(new Book(book.getName(),book.getAuthor()));

        } catch (Exception e){
            throw new RuntimeException("Erro na recepção da Mensagem: ", e);
        }

    }

}
