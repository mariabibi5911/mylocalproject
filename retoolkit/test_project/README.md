# NGI PRO analysis fixture

This directory is the generated output included in `retoolkit.zip` on the
remote `main` branch. It was produced from the repository APK (SHA-256
`7745e4ee4b41e6025a88649f3d409d06087b8f5a11f967f09012f760f8540d73`) using
the retoolkit pipeline with `arm64-v8a` selected for deep native analysis.

The JSON reports are useful reference data for the APK. The archive does not
include the raw APK or extracted `.so`/DEX inputs; use the repository APK and
run the pipeline to regenerate those folders. Some report fields contain the
original machine path used when the fixture was generated; those paths are
not required to read the reports and should not be treated as portable project
paths.
