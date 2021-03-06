package br.gov.servicos.orgao;

import br.gov.servicos.cms.PaginaEstatica;
import br.gov.servicos.servico.ServicoRepository;
import br.gov.servicos.v3.schema.OrgaoXML;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class OrgaoController {

    OrgaoRepository orgaoRepository;
    OrgaoRepositoryUtil orgaosRepositoryUtil;
    ServicoRepository servicos;

    @Autowired
    OrgaoController(OrgaoRepository orgaoRepository, OrgaoRepositoryUtil orgaosRepositoryUtil, ServicoRepository servicos) {
        this.orgaoRepository = orgaoRepository;
        this.orgaosRepositoryUtil = orgaosRepositoryUtil;
        this.servicos = servicos;
    }

    @RequestMapping("/orgaos")
    ModelAndView orgaos() {
        return new ModelAndView("orgaos", "orgaos", orgaosRepositoryUtil.findAll());
    }

    @RequestMapping("/orgao/{id}")
    public ModelAndView orgao(@PathVariable("id") OrgaoXML orgaoXML) {
        Map<String, Object> model = new HashMap<>();

        model.put("termo", orgaoXML.getId());
        model.put("conteudo", orgaoXML);

        model.put("resultados", servicos.findByOrgao(orgaoXML)
                .stream()
                .map(PaginaEstatica::fromServico)
                .sorted(comparing(PaginaEstatica::getId))
                .collect(toList()));

        return new ModelAndView("orgao", model);
    }
}
