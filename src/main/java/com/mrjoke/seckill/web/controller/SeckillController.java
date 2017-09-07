package com.mrjoke.seckill.web.controller;

import com.mrjoke.seckill.dto.Exposer;
import com.mrjoke.seckill.dto.SeckillExecution;
import com.mrjoke.seckill.dto.SeckillResult;
import com.mrjoke.seckill.entities.Seckill;
import com.mrjoke.seckill.exception.RepeatSeckillException;
import com.mrjoke.seckill.exception.SeckillCloseException;
import com.mrjoke.seckill.service.SeckillService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill")
public class SeckillController {
    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    /**
     * 获取秒杀商品列表接口
     *
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String list(Model model){
        List<Seckill> seckillList = seckillService.getSeckillList();
        model.addAttribute("seckillList",seckillList);
        return "list";
    }

    /**
     * 获取秒杀商品详情信息接口
     *
     * @param seckillId
     * @param model
     * @return
     */
    @GetMapping("/{seckillId}/details")
    public String details(@PathVariable("seckillId")int seckillId, Model model){
        Seckill seckill = seckillService.getSeckillById(seckillId);
        if (seckill == null){
            return "redirect:/seckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "details";
    }

    /**
     * 秒杀接口地址暴露接口
     *
     * @param seckillId
     */
    @PostMapping(value = "/{seckillId}/exposer",produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> expose(@PathVariable("seckillId") int seckillId){
        SeckillResult<Exposer> seckillResult;
        try {
            Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
            seckillResult = new SeckillResult<>(true,exposer);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            seckillResult = new SeckillResult<>(false,e.getMessage());
        }
        return seckillResult;
    }

    /**
     * 执行秒杀
     *
     * @param seckillId
     * @param md5
     * @param killPhone
     * @return
     */
    @PostMapping(value = "/{seckillId}/{md5}/execution",produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") int seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone",required = false)String killPhone){
        if (killPhone == null || killPhone.equals("")){
            return new SeckillResult<>(true,"未注册!!!");
        }
        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, killPhone, md5);
            return new SeckillResult<>(true,seckillExecution);
        }catch (SeckillCloseException | RepeatSeckillException e1){
            return new SeckillResult<>(true,e1.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new SeckillResult<>(false,e.getMessage());
        }
    }

    /**
     * 获取当前系统时间
     *
     * @return
     */
    @GetMapping(value = "/time/now")
    @ResponseBody
    public SeckillResult<Long> currentTime(){
        Date now = new Date();
        return new SeckillResult<>(true,now.getTime());
    }
}
