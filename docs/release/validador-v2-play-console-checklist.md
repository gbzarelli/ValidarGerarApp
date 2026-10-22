# Validador de documentos V3 — Play Console checklist

## Nova publicação gratuita (22/10/2026)

Foi criada uma única ficha gratuita com o título “Validador de documentos”, package novo `br.com.helpdev.validador.v3`, `versionName 3.0.0` e `versionCode 1`. A edição paga foi retirada do escopo e do build. Não enviar para as fichas V2 rejeitadas.

O produto faz somente validações locais de formato e dígitos verificadores para CPF, CNPJ (inclusive alfanumérico), PIS e RENAVAM; remove geração e validação de cartão; não consulta ou afirma existência, situação cadastral ou titularidade. A única edição Free exibe anúncios somente após o consentimento permitir.

**Bloqueio anterior confirmado na Console (22/10/2026):** as fichas Free e Paid mostram `Misleading Claims policy: Violation of Misleading Claims policy`, rejeitadas em 17/10/2026. Os detalhes identificaram descrições que ofereciam geração de documentos/cartões e orientaram, sem vínculo/autorização governamental, corrigir o app e usar **novo package name e novo app name**. A decisão agora é seguir essa orientação em uma única ficha Free com o package `.v3`.

**Paid removido:** a edição paga não será mantida nem reenviada. O antigo registro rejeitado fica preservado, sem novas releases; esta execução não tenta apagá-lo da Console.

**Privacidade:** a página pública `https://helpdev.com.br/apps/validar-gerar-documentos/privacidade/` foi corrigida em branch de site para refletir a edição gratuita de validação e segue `noindex`; aguardando PR, merge e deploy antes de qualquer envio à Play.

## App records

The two rejected records were created in the Help DEV personal developer account on 2026-10-14 with Portuguese (Brazil) as the default language. The original Paid title was “Validar e Gerar Documentos”; the original Free title was “Validar e Gerar Documentos Ads”. The V3 Free draft was created on 2026-10-22 after the package was confirmed available.

| Edition | Package | Play app ID | Status |
| --- | --- | --- | --- |
| Free V2 (rejected) | `br.com.helpdev.validador.v2.free` | `4975364428850056655` | Rejected; do not submit again |
| Paid V2 (rejected) | `br.com.helpdev.validador.v2` | `4975612393139412010` | Rejected; do not submit again; paid edition discontinued |
| New Free V3 | `br.com.helpdev.validador.v3` | `4975016217679467939` | Draft created; setup incomplete; no release submitted |

## Previous listing and release history

On 2026-10-14, the previous Portuguese (Brazil) listings and production releases were submitted for review. Both submissions were rejected on 2026-10-17. The policy details show that their descriptions offered number generation and the Paid described credit-card generation; those claims do not match the new validation-only scope. Previous releases were Brazil-only. No replacement AAB has been submitted.

The previous Paid listing stated that values were processed locally and there were no ads. The previous Free listing disclosed AdMob data use and linked the privacy page. Both descriptions must be rewritten for the new app before any new submission.

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
- Quatro capturas preliminares de telefone (`1080×1920`) foram carregadas na ficha da Play Console. Os arquivos não estão versionados neste repositório; não há capturas de tablet na ficha.
- **Antes de enviar a ficha à revisão, substituir as capturas preliminares:** foram feitas em debug e podem mostrar “Test Ad”. Gerar capturas de produção, conferir o fluxo com consentimento e evitar qualquer anúncio de teste.

The Free V3 production release has not yet been submitted for review. Store listing and app-content declarations are saved; the release AAB uses V3 AdMob IDs. The AdMob GDPR message editor is currently inaccessible (see GitHub issue #6), so the UMP message remains unconfigured. Keep distribution limited to Brazil and track the consent-editor issue separately. Do not upload to either rejected V2 record.
