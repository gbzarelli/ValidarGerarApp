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
- The existing AdMob app `Validar e Gerar Documentos V2 Free Android` is still marked **Requires review**. Do not reuse its production app/ad IDs in V3. New V3 AdMob app/unit and consent configuration are unverified.
- New V3 production ad identifiers must replace the old V2 IDs in the Free release resources before creating a release AAB.

## Ficha da Play e capturas de tela

Requisitos conferidos na ajuda oficial da Play Console em 22/10/2026 ([assets de prévia](https://support.google.com/googleplay/android-developer/answer/9866151?hl=pt-BR)):

- Para publicar, fornecer pelo menos 2 screenshots distribuídos entre tipos de dispositivo; arquivos JPEG ou PNG 24-bit sem alpha, entre 320 e 3840 px, sem dimensão maior que o dobro da menor.
- Para recomendação em superfícies grandes, usar pelo menos 4 screenshots de app em 1080 px ou mais: retrato 9:16 (mínimo 1080×1920) ou paisagem 16:9 (mínimo 1920×1080). Para tablet/Chromebook, a Console pede pelo menos 4 screenshots entre 1080 e 7680 px.
- Também são obrigatórios ícone 512×512 PNG com alpha (até 1 MB), feature graphic 1024×500 JPEG/PNG 24-bit sem alpha e descrição curta com até 80 caracteres.
- Capturas preliminares feitas no emulador Android 16: telefone `1080×1920` e tablet `1536×2732`, ambas em 9:16 e dentro dos limites dimensionais. Arquivos locais estão em `imagens/play-store-v3/`.
- **Não subir as capturas preliminares:** elas são do debug e podem mostrar “Test Ad”; além disso, a tela no tablet deixa muito espaço vazio. Refazer os assets depois do AdMob V3/UMP pronto, validar o layout tablet e remover/evitar qualquer anúncio de teste antes de enviar a ficha.

There is currently no release awaiting review. Before submitting the new Free V3 app, complete the store listing and app-content declarations, publish the corrected privacy page, create a release AAB with V3 AdMob IDs, and resolve AdMob review/consent for the new package. Do not upload to either rejected V2 record.
