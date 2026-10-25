# Validador de documentos V3 — Play Console checklist

## Correção da rejeição da V3 (25/10/2026)

Foi criada uma única ficha gratuita com o título “Validador de documentos” e package `br.com.helpdev.validador.v3`. A edição paga foi retirada do escopo e do build. A correção foi preparada nesta mesma ficha como `versionName 3.0.2` e `versionCode 3`; o code 2 do rascunho foi removido e substituído pelo AAB final. Não enviar para as fichas V2 rejeitadas.

O produto faz somente validações locais de formato e dígitos verificadores para CPF, CNPJ (inclusive alfanumérico), PIS e RENAVAM; remove geração e validação de cartão; não consulta ou afirma existência, situação cadastral ou titularidade. A única edição Free exibe anúncios somente após o consentimento permitir.

**Rejeição atual confirmada na Console (25/10/2026):** `Misleading Claims` — `Missing Source Link for Government Information` — apontado em `Full description (pt-BR)`. A descrição não tinha links diretos para fontes oficiais e não deixava claro que o app é independente e não representa órgão público. A V3 mantém a orientação da Console: uma única ficha Free, novo título/package e somente validação local.

**Correção proposta:** exibir no app e na descrição da Play um aviso explícito de independência/localidade e links para os canais oficiais de CPF (Receita Federal), CNPJ alfanumérico e cálculo de DV (Receita Federal), PIS/NIT (INSS) e RENAVAM (Senatran). Apresentá-los como canais de consulta externa, sem sugerir que o app consulte bases oficiais ou seja endossado por esses órgãos.

**Paid removido:** a edição paga não será mantida nem reenviada. O antigo registro rejeitado fica preservado, sem novas releases; esta execução não tenta apagá-lo da Console.

**Privacidade:** a página pública `https://helpdev.com.br/apps/validar-gerar-documentos/privacidade/` segue `noindex` e descreve a edição Free e o processamento local. O PR #28 do site foi mesclado em `main` (commit `7f30cf7`) e a tag `v1.3.10` foi enviada para disparar o deploy; validar o término da pipeline antes de considerar a página atualizada ao vivo.

## App records

The two rejected records were created in the Help DEV personal developer account on 2026-10-14 with Portuguese (Brazil) as the default language. The original Paid title was “Validar e Gerar Documentos”; the original Free title was “Validar e Gerar Documentos Ads”. The V3 Free draft was created on 2026-10-22 after the package was confirmed available.

| Edition | Package | Play app ID | Status |
| --- | --- | --- | --- |
| Free V2 (rejected) | `br.com.helpdev.validador.v2.free` | `4975364428850056655` | Rejected; do not submit again |
| Paid V2 (rejected) | `br.com.helpdev.validador.v2` | `4975612393139412010` | Rejected; do not submit again; paid edition discontinued |
| New Free V3 | `br.com.helpdev.validador.v3` | `4975016217679467939` | Version 3.0.0 rejected; 3.0.2/code 3 saved as a production-release draft, waiting for the consent gate before review |

## Previous listing and release history

On 2026-10-14, the previous Portuguese (Brazil) listings and production releases were submitted for review. Both submissions were rejected on 2026-10-17. The policy details show that their descriptions offered number generation and the Paid described credit-card generation; those claims do not match the new validation-only scope. Previous releases were Brazil-only. The V3 replacement AAB is saved as an unsubmitted draft and has not been sent for review.

The previous Paid listing stated that values were processed locally and there were no ads. The V3 pt-BR listing must clearly state the independent/local-only scope, cite official external channels, retain the privacy link, and disclose AdMob. It is being updated as part of version 3.0.2/code 3.

## Other current checks

- Play App Signing identity and Android developer verification still require verification for the new app record before release.
- The previous Paid price was saved at R$ 2,00 in Brazil with automatic regional conversions. The paid edition is discontinued; no paid price or product will be configured for V3.
- The existing AdMob app `Validar e Gerar Documentos V2 Free Android` remains **Requires review**; its IDs were not reused.
- AdMob V3 app `Validador de documentos V3 Android` was created as not yet listed (App ID `ca-app-pub-4334384724173584~6417805276`) with banner unit `ca-app-pub-4334384724173584/5605171079`. AdMob says a new unit may take up to an hour to start serving; link the app to the published Google Play listing later to start review and remove limited serving.
- The Free release resources now use the V3 app/banner IDs; the Free debug resources retain Google's official test IDs. UMP consent message configuration remains a release gate.

## Ficha da Play e capturas de tela

Requisitos conferidos na ajuda oficial da Play Console em 22/10/2026 ([assets de prévia](https://support.google.com/googleplay/android-developer/answer/9866151?hl=pt-BR)):

- Para publicar, fornecer pelo menos 2 screenshots distribuídos entre tipos de dispositivo; arquivos JPEG ou PNG 24-bit sem alpha, entre 320 e 3840 px, sem dimensão maior que o dobro da menor.
- Para recomendação em superfícies grandes, usar pelo menos 4 screenshots de app em 1080 px ou mais: retrato 9:16 (mínimo 1080×1920) ou paisagem 16:9 (mínimo 1920×1080). Para tablet/Chromebook, a Console pede pelo menos 4 screenshots entre 1080 e 7680 px.
- Também são obrigatórios ícone 512×512 PNG com alpha (até 1 MB), feature graphic 1024×500 JPEG/PNG 24-bit sem alpha e descrição curta com até 80 caracteres.
- A ficha contém duas capturas do Free (`1080×1920`, JPEG RGB sem alpha), geradas no emulador e salvas no rascunho da Play Console: `phone-01-local-validation.jpg` e `phone-02-invalid-cpf.jpg`, versionadas em `imagens/play-store-v3/`. A segunda usa apenas zeros para demonstrar uma entrada inválida e evitar expor um identificador completo potencialmente atribuído. Não há capturas de tablet.
- As capturas mostram a tela de validação local e o resultado de uma entrada obviamente inválida, sem anúncios de teste. Antes do envio à revisão, conferir a experiência final de consentimento e evitar qualquer anúncio de teste.

The V3 version 3.0.0 was submitted and rejected on 2026-10-25. The existing V3 production draft now contains the signed version 3.0.2/code 3 AAB; version 3.0.1/code 2 was removed. The Play Console accepted the AAB and reports the release ready to send for review. The AdMob Privacy & Messaging page is accessible, but no European consent message is configured; UMP remains a release gate per the prior decision to wait. Do not submit until the European consent message is configured and the final consent flow is checked. Distribution remains limited to the app's Brazil target; use only the V3 AdMob IDs, and do not upload to either rejected V2 record.
