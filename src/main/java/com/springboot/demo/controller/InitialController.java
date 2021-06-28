package com.springboot.demo.controller;

import com.springboot.demo.dao.model.Person;
import com.springboot.demo.dao.repository.PersonRepository;
import com.springboot.demo.dto.UpdateApiDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/initial")
public class InitialController {

    @Autowired
    private PersonRepository repo;

    /**
     * 测试新增
     */
    @PostMapping("/save")
    public void save(@RequestBody Person person) {
//        Person person = new Person(1L, "刘备", "蜀国", 18, DateUtil.parse("1990-01-02 03:04:05"), "刘备（161年－223年6月10日），即汉昭烈帝（221年－223年在位），又称先主，字玄德，东汉末年幽州涿郡涿县（今河北省涿州市）人，西汉中山靖王刘胜之后，三国时期蜀汉开国皇帝、政治家。\n刘备少年时拜卢植为师；早年颠沛流离，备尝艰辛，投靠过多个诸侯，曾参与镇压黄巾起义。先后率军救援北海相孔融、徐州牧陶谦等。陶谦病亡后，将徐州让与刘备。赤壁之战时，刘备与孙权联盟击败曹操，趁势夺取荆州。而后进取益州。于章武元年（221年）在成都称帝，国号汉，史称蜀或蜀汉。《三国志》评刘备的机权干略不及曹操，但其弘毅宽厚，知人待士，百折不挠，终成帝业。刘备也称自己做事“每与操反，事乃成尔”。\n章武三年（223年），刘备病逝于白帝城，终年六十三岁，谥号昭烈皇帝，庙号烈祖，葬惠陵。后世有众多文艺作品以其为主角，在成都武侯祠有昭烈庙为纪念。");
        Person save = repo.save(person);
        log.info("【save】= {}", save);
    }

    /**
     * 测试批量新增
     */
    @PostMapping("/saveList")
    public void saveList(@RequestBody List<Person> personList) {
        Iterable<Person> people = repo.saveAll(personList);
        log.info("【people】= {}", people);
    }

    /**
     * 测试更新
     *
     * @param updateApiDTO
     */
    @PostMapping("/update")
    public void update(@RequestBody UpdateApiDTO updateApiDTO) {
        repo.findById(Long.parseLong(updateApiDTO.getDocId())).ifPresent(person -> {
//            person.setRemark(person.getRemark() + "\n更新更新更新更新更新");
            Person save = repo.save(person);
            log.info("【save】= {}", save);
        });
    }

    /**
     * 测试普通查询，按生日倒序
     */
    @PostMapping("/select")
    public List<Person> select() {
//        repo.findAll(Sort.by(Sort.Direction.DESC, "birthday")).forEach(person -> log.info("{} 生日", person.getName()));
        Iterable<Person> personIterable = repo.findAll();
        List<Person> personList = new ArrayList<>();
        personIterable.forEach(personList::add);
        return personList;
    }
}
