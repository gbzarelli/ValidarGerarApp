# Validar e Gerar v2 — Design

## Objetivo

Modernizar o aplicativo Android Validar e Gerar e preparar duas novas publicações na Google Play: uma edição paga sem anúncios e uma edição gratuita com anúncios. As duas serão novos aplicativos, com novos identificadores de pacote e o mesmo nome público de publicação. O resultado deve compilar e funcionar nas versões Android suportadas, cumprir as exigências atuais da Play e permitir validar os fluxos em teste fechado antes da publicação em produção.

## Decisões aprovadas

| Variante | Identificador do pacote | Distribuição | Monetização |
|---|---|---|---|
| `paid` | `br.com.helpdev.validador.v2` | Novo aplicativo Google Play pago | Sem SDK, configuração ou interface de anúncios |
| `free` | `br.com.helpdev.validador.v2.free` | Novo aplicativo Google Play gratuito | Banner Google AdMob |

- Manter o nome público “Validar e Gerar Documentos” nas duas fichas, se a Play Console aceitar nomes iguais para os novos apps.
- Manter um único projeto Android e suas duas product flavors.
- Usar `minSdk 24` (Android 7.0) nas duas variantes. Esta decisão substitui a aprovação anterior de API 23 porque o Google Mobile Ads SDK 25.5.0 requer API 24.
- Usar `compileSdk 36` e `targetSdk 36` como baseline de envio, conforme a exigência de API alvo observada na conta e na política da Play vigente em 14 de outubro de 2026.
- Criar fichas novas na Play para os dois pacotes. Não reutilizar as fichas antigas: a ficha gratuita antiga foi removida e a paga está suspensa.
- Habilitar Play App Signing para cada novo app e gerar chaves de upload novas, guardadas fora do repositório. Não depender dos keystores antigos nem de arquivo de senha legado.
- Criar no AdMob um novo registro da edição `free` associado ao pacote `v2.free`, com novo App ID e unidade de banner. O ID de unidade existente pertence ao app/pacote antigo e não será copiado para produção.
- Publicar primeiro em teste fechado. Envio a produção fica condicionado à verificação dos AABs, testes nos dispositivos-alvo, revisão dos dados da loja e aprovação do teste fechado.

## Estado conhecido e restrições

- O projeto usa Android Gradle Plugin 3.2.1, Gradle Wrapper 4.6, Kotlin 1.3.11, compile/target SDK 28, min SDK 14, Android Support 28 e Google Mobile Ads/Firebase Ads 17.1.2. O wrapper falha com o JDK 21 instalado, antes da configuração do projeto.
- O código de anúncios está apenas na flavor `free`, em `app/src/free/java/br/com/helpdev/validador/FragValidadores.kt`; o layout da variante contém `AdView`. A implementação não inclui a configuração atual de App ID do AdMob nem inicialização explícita moderna. A unidade de banner no AdMob corresponde ao pacote antigo `br.com.helpdev.validador.free`.
- A ficha antiga gratuita foi removida por falta do formulário de Segurança dos Dados; a Play Console também mostra exigências de target API 35 para disponibilidade e API 36 para envio de updates.
- A ficha antiga paga está suspensa desde 28 de setembro de 2018. A Play indica que o motivo detalhado foi enviado ao e-mail do proprietário.
- A página Android developer verification da conta mostra pacotes não registrados e alerta sobre a data de 30 de setembro de 2026. Os novos identificadores deverão ser verificados/registrados no fluxo disponível para a conta antes de concluir a publicação.
- A política do AdMob não mostra problemas atuais de veiculação, e a unidade antiga é listada, mas sem atividade. A home do AdMob mostra a etapa “Verify your app”; a verificação e aprovação do novo app ainda serão necessárias para veicular anúncios.
- A lista de apps da Play Console não retorna os pacotes `v2` pesquisados. Isso confirma apenas que não constam entre os apps da conta atual; a disponibilidade global dos IDs deve ser confirmada quando as novas fichas forem criadas.
- A leitura dos keystores antigos falhou com a combinação de arquivo de senhas encontrada; nenhum segredo deve ser impresso em logs ou copiado para o repositório. Confirmar fingerprints e propriedade só se isso for útil para registrar chaves antigas de distribuição direta no Android developer verification.
- O projeto ainda não tem evidência de compatibilidade da dependência `LibValidadores` com AndroidX/AGP atuais nem cobertura de testes significativa para os fluxos de documento.

## Arquitetura e componentes

### Build Android

Atualizar Gradle Wrapper, Android Gradle Plugin, Kotlin e plugins Google para versões estáveis compatíveis entre si e com JDK 17 ou superior. A baseline pesquisada para esta atualização é Gradle 9.6.0, Android Gradle Plugin 9.4.0 e Kotlin Gradle Plugin 2.4.10. Migrar as bibliotecas Android Support para AndroidX. Fixar `compileSdk 36`, `targetSdk 36` e `minSdk 24`. A migração preservará o desenho atual de um módulo e duas flavors, evitando reestruturação que não seja necessária para compilar e publicar.

Revisar a dependência `LibValidadores` e o uso de Kotlin synthetic Android Extensions. Substituir APIs removidas ou incompatíveis, atualizar recursos/manifestos e corrigir avisos que afetem funcionamento ou submissão. Manter shrink/minify de release apenas depois de provar que os fluxos continuam funcionando no artefato minificado.

Auditar se Firebase é utilizado além do SDK antigo de anúncios. Remover o Google Services plugin e os arquivos `google-services.json` das variantes se não houver serviços Firebase ativos; caso algum serviço seja encontrado em uso real, migrar somente esse serviço e manter sua configuração por flavor.

### Anúncios e privacidade

Atualizar a integração da flavor `free` para `com.google.android.gms:play-services-ads:25.5.0`, que requer `minSdk 24`. Obter do novo registro AdMob o App ID e a unidade de banner e injetá-los por recursos/manifest da flavor `free`, sem valores de produção na variante `paid`. Inicializar o SDK uma vez no ciclo de vida apropriado e gerenciar o ciclo de vida do banner.

Integrar o fluxo de consentimento exigido para regiões aplicáveis usando a solução atual do Google para consentimento. Atualizar a política de privacidade e as declarações de Segurança dos Dados com base nos SDKs e no comportamento efetivamente observado no AAB. Não publicar IDs de teste como IDs de produção; manter IDs de teste nos builds de desenvolvimento/teste e usar o novo ID somente após a validação AdMob.

### Identidade e assinatura

Definir os novos `applicationId` por flavor exatamente como aprovados. Manter as chaves privadas fora do Git e documentar processo de backup seguro. Ativar Play App Signing em cada app novo, guardar as chaves de upload e fingerprints em armazenamento local seguro, e registrar certificados exigidos pela Play/Android developer verification. Fazer upload inicial de AABs por teste fechado e confirmar que a Play reconhece a assinatura e a identidade correta de cada variante.

## Fluxo de publicação

1. Corrigir e modernizar o build; gerar variantes debug e release; inspecionar manifests, dependências e AABs para confirmar package, versão, permissões e ausência de anúncios na edição paga.
2. Criar dois apps na Play Console, mantendo o nome público “Validar e Gerar Documentos” se aceito, com os respectivos pacotes `v2`. Criar/selecionar os registros de produto e preços para a variante paga. Confirmar disponibilidade global dos pacotes durante esse passo.
3. Habilitar Play App Signing para cada app e registrar as novas chaves/pacotes no fluxo Android developer verification antes dos prazos e requisitos mostrados pela Console.
4. Completar para cada app os itens de configuração de Play: detalhes da loja, classificação etária, público-alvo, declarações de anúncios, conteúdo do app, Segurança dos Dados, política de privacidade, países e regiões. As declarações devem corresponder ao conteúdo de cada AAB.
5. Criar e verificar no AdMob o novo app Android correspondente a `br.com.helpdev.validador.v2.free`, criar a unidade de banner e a mensagem de consentimento. Integrar os IDs gerados e aguardar o estado de aprovação/verificação necessário.
6. Enviar cada AAB para teste fechado e concluir os requisitos de teste exibidos pela conta Play. Testar instalação, abertura, orientação/telas principais, validação e geração de documentos, fluxo de consentimento, retorno do banner e ausência de SDK/requisições de anúncios na flavor paga.
7. Revisar os resultados do pré-lançamento e os avisos de política/vitals. Corrigir falhas antes do envio a revisão.
8. Após o teste fechado, revisar preço e disponibilidade por país da variante paga; publicar os dois apps em produção e confirmar a disponibilidade pública e o comportamento final.

Criação de fichas, chaves de assinatura, unidades de anúncio, uploads e envio a revisão são operações externas com impacto duradouro. A execução do plano deve apresentar uma confirmação específica imediatamente antes de cada ação desse tipo, com destino e consequência claros. Até lá, o plano pode preparar os artefatos locais e reunir todos os dados necessários.

## Tratamento de falhas

- Se a dependência de validação não funcionar com o toolchain moderno, localizar a incompatibilidade e decidir entre uma atualização compatível da biblioteca ou substituição mínima, preservando a semântica documentada/testada dos validadores.
- Se o AdMob não aprovar/verificar o novo app, pausar a habilitação de anúncios de produção, registrar o aviso exibido, corrigir ficha/loja/integração e repetir a validação com anúncios de teste.
- Se a Play rejeitar package, nome, AAB ou declaração de política, seguir a causa específica apresentada pela Console; não criar pacotes alternativos sem decisão explícita.
- Se Play App Signing ou Android developer verification solicitar chaves legadas, inspecionar certificados antigos sem revelar senhas e avaliar o cadastro separado das chaves de distribuição direta; as novas apps continuam assinadas segundo o esquema Play.
- Se os requisitos de teste fechado variarem pela classificação/conta, seguir os números e prazos apresentados na Console para a conta Help DEV e registrar esses valores como gates de lançamento.

## Critérios de aceite

- `freeDebug`, `paidDebug`, `freeRelease` e `paidRelease` configuram e compilam com o toolchain suportado pelo projeto; releases produzem AABs instaláveis/aceitos nos fluxos de teste da Play.
- Os AABs declaram os pacotes exatos aprovados, target API 36 e min API 24.
- A edição paga não contém o SDK Mobile Ads, IDs, views, inicialização ou solicitações de anúncio. A edição gratuita inicializa o SDK corretamente, solicita consentimento quando necessário e carrega o banner sem erro de configuração.
- Os fluxos centrais de validação e geração de documentos passam testes automatizados e validação manual em Android 7 (API 24) e Android 16 (API 36), incluindo release minificado.
- Play App Signing, chaves de upload e registro Android developer verification estão prontos para ambos os apps; nenhum material secreto está versionado.
- As fichas da Play e do AdMob estão preenchidas com informações compatíveis com o app final; teste fechado e pré-lançamento não mostram falhas bloqueadoras.
- A produção só é considerada concluída quando ambas as fichas aprovarem as revisões e estiverem disponíveis no Google Play.

## Fora de escopo

- Recuperar, recorrer ou republicar nos pacotes antigos `br.com.helpdev.validador` e `br.com.helpdev.validador.free`.
- Alterar apps não relacionados existentes na conta Play ou AdMob.
- Publicar para distribuição fora da Google Play ou criar campanhas pagas de Google Ads.
- Reescrever a experiência visual do app sem necessidade para compatibilidade, privacidade ou qualidade de publicação.

