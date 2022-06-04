package io.github.valacuz.proguard.dictionary.util

import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logging
import org.slf4j.Marker

/**
 * Thanks to ahasbini.
 *
 * see more: https://gist.github.com/ahasbini/d1d7f8a9af4bb38c79430a31c8673e80.
 */
class Logger(
    private val logger: org.gradle.api.logging.Logger,
) : org.gradle.api.logging.Logger {

    companion object {

        fun getLogger(clazz: Class<*>): Logger {
            return Logger(Logging.getLogger(clazz))
        }
    }

    var useQuietLog: Boolean = false

    override fun getName(): String = logger.name

    override fun isTraceEnabled(): Boolean = logger.isTraceEnabled

    override fun isTraceEnabled(p0: Marker?): Boolean = logger.isTraceEnabled(p0)

    override fun trace(p0: String?) = logger.trace(p0)

    override fun trace(p0: String?, p1: Any?) = logger.trace(p0, p1)

    override fun trace(p0: String?, p1: Any?, p2: Any?) = logger.trace(p0, p1, p2)

    override fun trace(p0: String?, vararg p1: Any?) = logger.trace(p0, p1)

    override fun trace(p0: String?, p1: Throwable?) = logger.trace(p0, p1)

    override fun trace(p0: Marker?, p1: String?) = logger.trace(p0, p1)

    override fun trace(p0: Marker?, p1: String?, p2: Any?) = logger.trace(p0, p1, p2)

    override fun trace(p0: Marker?, p1: String?, p2: Any?, p3: Any?) = logger.trace(p0, p1, p2, p3)

    override fun trace(p0: Marker?, p1: String?, vararg p2: Any?) = logger.trace(p0, p1)

    override fun trace(p0: Marker?, p1: String?, p2: Throwable?) = logger.trace(p0, p1, p2)

    override fun isDebugEnabled(): Boolean = logger.isDebugEnabled

    override fun isDebugEnabled(p0: Marker?): Boolean = logger.isDebugEnabled(p0)

    override fun debug(p0: String?, vararg p1: Any?) {
        if (useQuietLog) {
            logger.quiet(p0, p1)
        } else {
            logger.debug(p0, p1)
        }
    }

    override fun debug(p0: String?) {
        if (useQuietLog) {
            logger.quiet(p0)
        } else {
            logger.debug(p0)
        }
    }

    override fun debug(p0: String?, p1: Any?) {
        if (useQuietLog) {
            logger.quiet(p0, p1)
        } else {
            logger.debug(p0, p1)
        }
    }

    override fun debug(p0: String?, p1: Any?, p2: Any?) {
        if (useQuietLog) {
            logger.quiet(p0, p1, p2)
        } else {
            logger.debug(p0, p1, p2)
        }
    }

    override fun debug(p0: String?, p1: Throwable?) {
        if (useQuietLog) {
            logger.quiet(p0, p1)
        } else {
            logger.debug(p0, p1)
        }
    }

    override fun debug(p0: Marker?, p1: String?) = logger.debug(p0, p1)

    override fun debug(p0: Marker?, p1: String?, p2: Any?) = logger.debug(p0, p1, p2)

    override fun debug(p0: Marker?, p1: String?, p2: Any?, p3: Any?) = logger.debug(p0, p1, p2, p3)

    override fun debug(p0: Marker?, p1: String?, vararg p2: Any?) = logger.debug(p0, p1, p2)

    override fun debug(p0: Marker?, p1: String?, p2: Throwable?) = logger.debug(p0, p1, p2)

    override fun isInfoEnabled(): Boolean = logger.isInfoEnabled

    override fun isInfoEnabled(p0: Marker?): Boolean = logger.isInfoEnabled(p0)

    override fun info(p0: String?, vararg p1: Any?) {
        if (useQuietLog) {
            logger.quiet(p0, p1)
        } else {
            logger.info(p0, p1)
        }
    }

    override fun info(p0: String?) {
        if (useQuietLog) {
            logger.quiet(p0)
        } else {
            logger.info(p0)
        }
    }

    override fun info(p0: String?, p1: Any?) {
        if (useQuietLog) {
            logger.quiet(p0, p1)
        } else {
            logger.info(p0, p1)
        }
    }

    override fun info(p0: String?, p1: Any?, p2: Any?) {
        if (useQuietLog) {
            logger.quiet(p0, p1)
        } else {
            logger.info(p0, p1)
        }
    }

    override fun info(p0: String?, p1: Throwable?) {
        if (useQuietLog) {
            logger.quiet(p0, p1)
        } else {
            logger.info(p0, p1)
        }
    }

    override fun info(p0: Marker?, p1: String?) = logger.info(p0, p1)

    override fun info(p0: Marker?, p1: String?, p2: Any?) = logger.info(p0, p1, p2)

    override fun info(p0: Marker?, p1: String?, p2: Any?, p3: Any?) = logger.info(p0, p1, p2, p3)

    override fun info(p0: Marker?, p1: String?, vararg p2: Any?) = logger.info(p0, p1, p2)

    override fun info(p0: Marker?, p1: String?, p2: Throwable?) = logger.info(p0, p1, p2)

    override fun isWarnEnabled(): Boolean = logger.isWarnEnabled

    override fun isWarnEnabled(p0: Marker?): Boolean = logger.isWarnEnabled(p0)

    override fun warn(p0: String?) {
        if (useQuietLog) {
            logger.quiet(p0)
        } else {
            logger.warn(p0)
        }
    }

    override fun warn(p0: String?, p1: Any?) {
        if (useQuietLog) {
            logger.quiet(p0, p1)
        } else {
            logger.warn(p0, p1)
        }
    }

    override fun warn(p0: String?, vararg p1: Any?) {
        if (useQuietLog) {
            logger.quiet(p0, p1)
        } else {
            logger.warn(p0, p1)
        }
    }

    override fun warn(p0: String?, p1: Any?, p2: Any?) {
        if (useQuietLog) {
            logger.quiet(p0, p1, p2)
        } else {
            logger.warn(p0, p1, p2)
        }
    }

    override fun warn(p0: String?, p1: Throwable?) {
        if (useQuietLog) {
            logger.quiet(p0, p1)
        } else {
            logger.warn(p0, p1)
        }
    }

    override fun warn(p0: Marker?, p1: String?) = logger.warn(p0, p1)

    override fun warn(p0: Marker?, p1: String?, p2: Any?) = logger.warn(p0, p1, p2)

    override fun warn(p0: Marker?, p1: String?, p2: Any?, p3: Any?) = logger.warn(p0, p1, p2)

    override fun warn(p0: Marker?, p1: String?, vararg p2: Any?) = logger.warn(p0, p1, p2)

    override fun warn(p0: Marker?, p1: String?, p2: Throwable?) = logger.warn(p0, p1, p2)

    override fun isErrorEnabled(): Boolean = logger.isErrorEnabled

    override fun isErrorEnabled(p0: Marker?): Boolean = logger.isErrorEnabled(p0)

    override fun error(p0: String?) {
        if (useQuietLog) {
            logger.quiet(p0)
        } else {
            logger.error(p0)
        }
    }

    override fun error(p0: String?, p1: Any?) {
        if (useQuietLog) {
            logger.quiet(p0, p1)
        } else {
            logger.error(p0, p1)
        }
    }

    override fun error(p0: String?, p1: Any?, p2: Any?) {
        if (useQuietLog) {
            logger.quiet(p0, p1, p2)
        } else {
            logger.error(p0, p1, p2)
        }
    }

    override fun error(p0: String?, vararg p1: Any?) {
        if (useQuietLog) {
            logger.quiet(p0, p1)
        } else {
            logger.error(p0, p1)
        }
    }

    override fun error(p0: String?, p1: Throwable?) {
        if (useQuietLog) {
            logger.quiet(p0, p1)
        } else {
            logger.error(p0, p1)
        }
    }

    override fun error(p0: Marker?, p1: String?) = logger.error(p0, p1)

    override fun error(p0: Marker?, p1: String?, p2: Any?) = logger.error(p0, p1, p2)

    override fun error(p0: Marker?, p1: String?, p2: Any?, p3: Any?) = logger.error(p0, p1, p2, p3)

    override fun error(p0: Marker?, p1: String?, vararg p2: Any?) = logger.error(p0, p1, p2)

    override fun error(p0: Marker?, p1: String?, p2: Throwable?) = logger.error(p0, p1, p2)

    override fun isLifecycleEnabled(): Boolean = logger.isLifecycleEnabled

    override fun lifecycle(p0: String?) {
        if (useQuietLog) {
            logger.quiet(p0)
        } else {
            logger.lifecycle(p0)
        }
    }

    override fun lifecycle(p0: String?, vararg p1: Any?) {
        if (useQuietLog) {
            logger.quiet(p0, p1)
        } else {
            logger.lifecycle(p0, p1)
        }
    }

    override fun lifecycle(p0: String?, p1: Throwable?) {
        if (useQuietLog) {
            logger.quiet(p0, p1)
        } else {
            logger.lifecycle(p0, p1)
        }
    }

    override fun isQuietEnabled(): Boolean = logger.isQuietEnabled

    override fun quiet(p0: String?) = logger.quiet(p0)

    override fun quiet(p0: String?, vararg p1: Any?) = logger.quiet(p0, p1)

    override fun quiet(p0: String?, p1: Throwable?) = logger.quiet(p0, p1)

    override fun isEnabled(p0: LogLevel?): Boolean = logger.isEnabled(p0)

    override fun log(p0: LogLevel?, p1: String?) = logger.log(p0, p1)

    override fun log(p0: LogLevel?, p1: String?, vararg p2: Any?) = logger.log(p0, p1)

    override fun log(p0: LogLevel?, p1: String?, p2: Throwable?) = logger.log(p0, p1, p2)
}